package net.mrrites.fnafmod.client;

import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton менеджер загрузки.
 * - Попытка сохранить в C:\media, если нет доступа — fallback на runDirectory/media.
 * - Отслеживает последнее время завершения загрузки, чтобы показать сообщение "finished" в HUD короткое время.
 */
public class DownloadManager {
    private static final DownloadManager INSTANCE = new DownloadManager();

    // TODO: замените на ваш реальный URL и имя
    public static String DOWNLOAD_URL = "https://lunarylauncher.ru/content/media/cinema/movie/FNAF.mp4";
    public static String TARGET_FILENAME = "FNAF.mp4";

    private static final Path PREFERRED_DIR = Paths.get("C:", "media");
    private static final long SHOW_COMPLETE_MS = 5_000L; // показываем сообщение о завершении 5 секунд

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "media-downloader");
        t.setDaemon(true);
        return t;
    });

    private volatile DownloadTask currentTask;
    // время в millis, когда мы впервые обнаружили, что загрузка завершилась
    private volatile long lastCompleteMillis = 0L;

    private DownloadManager() {}

    public static DownloadManager getInstance() {
        return INSTANCE;
    }

    private Path resolveDownloadDir() throws IOException {
        try {
            if (!Files.exists(PREFERRED_DIR)) {
                Files.createDirectories(PREFERRED_DIR);
            }
            // проверим права записи
            Path probe = PREFERRED_DIR.resolve(".probe");
            Files.write(probe, new byte[]{0});
            Files.deleteIfExists(probe);
            return PREFERRED_DIR;
        } catch (Exception ignored) {
            // fallback: папка игры
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                Path gameDir = client.runDirectory.toPath().resolve("media");
                if (!Files.exists(gameDir)) {
                    Files.createDirectories(gameDir);
                }
                return gameDir;
            } else {
                Path cwdMedia = Paths.get("media");
                if (!Files.exists(cwdMedia)) Files.createDirectories(cwdMedia);
                return cwdMedia;
            }
        }
    }

    /**
     * Проверяет наличие финального файла в стандартных директориях (предпочтительная и fallback).
     * Возвращает true, если файл локально присутствует (независимо от того, совпадает ли размер с удаленным).
     */
    public boolean isFilePresentLocally() {
        try {
            Path preferred = PREFERRED_DIR.resolve(TARGET_FILENAME);
            if (Files.exists(preferred)) return true;
        } catch (Exception ignored) {}
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                Path gameDir = client.runDirectory.toPath().resolve("media").resolve(TARGET_FILENAME);
                if (Files.exists(gameDir)) return true;
            }
        } catch (Exception ignored) {}
        // также проверим в рабочей директории ./media
        try {
            Path cwd = Paths.get("media").resolve(TARGET_FILENAME);
            if (Files.exists(cwd)) return true;
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Запускает загрузку, если нужно. Если файл уже есть локально — не запускаем.
     */
    public synchronized void startDownloadIfNeeded() {
        // Если файл уже локально присутствует — ничего не делаем
        if (isFilePresentLocally()) {
            // Сбрасываем состояние completion — чтобы HUD не показывался
            lastCompleteMillis = 0L;
            return;
        }

        // Если уже идёт загрузка — ничего не делаем
        if (currentTask != null && currentTask.isRunning()) return;

        try {
            Path dir = resolveDownloadDir();
            DownloadTask task = new DownloadTask(DOWNLOAD_URL, dir, TARGET_FILENAME);
            this.currentTask = task;
            // Сбрасываем отметку о завершении при новом запуске
            lastCompleteMillis = 0L;

            // Если задача считает, что файл уже полностью скачан (например, final файл уже лежит с нужным размером),
            // то не запускаем поток.
            if (task.isAlreadyComplete()) {
                // Установим timestamp завершения, чтобы показать короткое сообщение о завершении
                lastCompleteMillis = System.currentTimeMillis();
                return;
            }

            executor.submit(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Попросить текущую задачу корректно остановиться (с сохранением .part).
     */
    public synchronized void pauseDownload() {
        if (currentTask != null) {
            currentTask.requestStop();
        }
    }

    public double getProgressFraction() {
        DownloadTask t = currentTask;
        if (t == null) return 0.0;
        long total = t.getTotalBytes();
        long downloaded = t.getDownloadedBytes();
        if (total <= 0) {
            if (downloaded <= 0) return 0.0;
            return 0.5; // неизвестно — показать половину (или можно по байтам)
        }
        return Math.min(1.0, (double) downloaded / (double) total);
    }

    public String getStatusText() {
        DownloadTask t = currentTask;
        if (t == null) return "";
        return t.getStatusText();
    }

    public long getTotalBytes() {
        DownloadTask t = currentTask;
        return t == null ? -1L : t.getTotalBytes();
    }

    public long getDownloadedBytes() {
        DownloadTask t = currentTask;
        return t == null ? 0L : t.getDownloadedBytes();
    }

    public boolean isDownloading() {
        DownloadTask t = currentTask;
        return t != null && t.isRunning();
    }

    /**
     * Проверяет и обновляет внутренний таймер завершения, если текущая задача перешла в Complete.
     * Используется HudOverlay, чтобы показывать короткое сообщение о завершении и затем скрывать HUD.
     */
    private void pollCompletion() {
        DownloadTask t = currentTask;
        if (t != null && "Complete".equals(t.getStatusText())) {
            if (lastCompleteMillis == 0L) {
                lastCompleteMillis = System.currentTimeMillis();
            }
        }
    }

    /**
     * Возвращает true если HUD должен отображаться:
     * - во время загрузки
     * - или в течение SHOW_COMPLETE_MS миллисекунд после того, как загрузка завершилась
     * В остальных случаях — false (включая когда файл уже скачан до захода).
     */
    public boolean shouldShowHud() {
        // если локально уже есть файл и ничего не скачивается — не показываем HUD
        if (isFilePresentLocally() && !isDownloading()) {
            return false;
        }

        if (isDownloading()) return true;

        // если загрузка завершилась недавно — показать сообщение
        pollCompletion();
        if (lastCompleteMillis > 0L) {
            long elapsed = System.currentTimeMillis() - lastCompleteMillis;
            if (elapsed <= SHOW_COMPLETE_MS) return true;
            // время показa вышло — сбрасываем отметку
            lastCompleteMillis = 0L;
        }
        return false;
    }

    /**
     * Если есть недавно завершённая загрузка — возвращает true и текст сообщения.
     */
    public boolean isRecentlyCompleted() {
        if (lastCompleteMillis == 0L) return false;
        long elapsed = System.currentTimeMillis() - lastCompleteMillis;
        return elapsed <= SHOW_COMPLETE_MS;
    }
}