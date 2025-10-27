package net.mrrites.fnafmod.client;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Runnable, который качает файл с поддержкой HTTP Range (resume).
 * Пишет во временный файл filename.part. При успешном завершении переименовывает в filename.
 */
public class DownloadTask implements Runnable {
    private final String url;
    private final Path directory;
    private final String filename;

    private volatile boolean stopRequested = false;
    private volatile boolean running = false;
    private volatile long downloadedBytes = 0;
    private volatile long totalBytes = -1;
    private volatile String statusText = "Not started";

    public DownloadTask(String url, Path directory, String filename) throws IOException {
        this.url = url;
        this.directory = directory;
        this.filename = filename;

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    public void requestStop() {
        stopRequested = true;
        statusText = "Paused";
    }

    public boolean isRunning() {
        return running;
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public String getStatusText() {
        return statusText;
    }

    /**
     * Проверяет, есть ли уже файл и совпадает ли размер с удалённым (HEAD).
     */
    public boolean isAlreadyComplete() {
        Path finalPath = directory.resolve(filename);
        try {
            if (Files.exists(finalPath)) {
                long remoteSize = queryRemoteContentLength();
                long localSize = Files.size(finalPath);
                if (remoteSize > 0 && localSize >= remoteSize) {
                    statusText = "Complete";
                    downloadedBytes = localSize;
                    totalBytes = remoteSize;
                    return true;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    @Override
    public void run() {
        running = true;
        stopRequested = false;
        statusText = "Starting";
        Path finalPath = directory.resolve(filename);
        Path tempPath = directory.resolve(filename + ".part");

        try {
            long existing = Files.exists(tempPath) ? Files.size(tempPath) : 0;

            // Получаем remote total
            long remoteSize = queryRemoteContentLength();
            if (remoteSize > 0) totalBytes = remoteSize;

            // Если финальный файл уже есть и его размер соответствует — заканчиваем
            if (Files.exists(finalPath) && totalBytes > 0 && Files.size(finalPath) >= totalBytes) {
                downloadedBytes = Files.size(finalPath);
                statusText = "Complete";
                running = false;
                return;
            }

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            if (existing > 0) {
                conn.setRequestProperty("Range", "bytes=" + existing + "-");
            }
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.connect();

            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK && code != HttpURLConnection.HTTP_PARTIAL) {
                statusText = "Server response: " + code;
                running = false;
                conn.disconnect();
                return;
            }

            // Попытаемся узнать общий размер через Content-Range или Content-Length
            String contentRange = conn.getHeaderField("Content-Range");
            if (contentRange != null && contentRange.contains("/")) {
                try {
                    String totalStr = contentRange.substring(contentRange.indexOf('/') + 1).trim();
                    totalBytes = Long.parseLong(totalStr);
                } catch (Exception ignored) {}
            } else {
                long contentLen = conn.getContentLengthLong();
                if (contentLen > 0) {
                    totalBytes = existing > 0 ? existing + contentLen : contentLen;
                }
            }

            downloadedBytes = existing;
            statusText = "Downloading";

            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                 RandomAccessFile raf = new RandomAccessFile(tempPath.toFile(), "rw")) {

                raf.seek(existing);

                byte[] buffer = new byte[64 * 1024]; // 64KB buffer
                int read;
                while ((read = in.read(buffer)) != -1) {
                    if (stopRequested) {
                        statusText = "Paused";
                        running = false;
                        conn.disconnect();
                        return;
                    }
                    raf.write(buffer, 0, read);
                    downloadedBytes += read;
                }
            }

            // Переименовываем .part -> final
            File tmp = tempPath.toFile();
            File fin = finalPath.toFile();
            if (tmp.exists()) {
                Files.move(tmp.toPath(), fin.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            statusText = "Complete";
            running = false;
        } catch (Exception e) {
            statusText = "Error: " + e.getClass().getSimpleName();
            e.printStackTrace();
            running = false;
        }
    }

    /**
     * HEAD-запрос для получения Content-Length (или -1 если неизвестно).
     */
    private long queryRemoteContentLength() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                long len = conn.getContentLengthLong();
                conn.disconnect();
                return len > 0 ? len : -1;
            } else {
                conn.disconnect();
                return -1;
            }
        } catch (IOException e) {
            return -1;
        }
    }
}