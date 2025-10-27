package net.mrrites.fnafmod.client;


import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * HUD overlay: показывает прогресс-бар ТОЛЬКО во время загрузки.
 * Если загрузка только что завершилась, показывает сообщение о завершении в течение короткого времени.
 */
public class HudOverlay implements HudRenderCallback {

    private static final HudOverlay INSTANCE = new HudOverlay();

    public static void register() {
        HudRenderCallback.EVENT.register(INSTANCE);
    }

    @Override
    public void onHudRender(net.minecraft.client.gui.DrawContext drawContext, net.minecraft.client.render.RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        DownloadManager dm = DownloadManager.getInstance();

        // Отрисовываем HUD только если менеджер говорит показывать
        if (!dm.shouldShowHud()) return;

        // Если только что завершилось — показываем простое сообщение "Download finished"
        if (!dm.isDownloading() && dm.isRecentlyCompleted()) {
            String doneText = "Скачивание завершено";
            int textWidth = client.textRenderer.getWidth(Text.literal(doneText));
            int x = (client.getWindow().getScaledWidth() - textWidth) / 2;
            int y = 10;
            drawContext.drawText(client.textRenderer, Text.literal(doneText), x, y, 0x55FF55, true);
            return;
        }

        // Иначе — если идёт загрузка — отрисовываем прогресс-бар (символьный)
        if (dm.isDownloading()) {
            double frac = dm.getTotalBytes() > 0 ? (double) dm.getDownloadedBytes() / dm.getTotalBytes() : (dm.getDownloadedBytes() > 0 ? 0.5 : 0.0);
            frac = Math.max(0.0, Math.min(1.0, frac));
            int percent = (int) Math.round(frac * 100.0);

            final int BAR_LENGTH = 24;
            int filled = (int) Math.round(frac * BAR_LENGTH);
            StringBuilder bar = new StringBuilder(BAR_LENGTH);
            for (int i = 0; i < BAR_LENGTH; i++) {
                if (i < filled) bar.append('█');
                else bar.append('░');
            }

            String status = dm.getStatusText() == null ? "" : dm.getStatusText();
            String text = "Скачивание контента: [" + bar + "] " + percent + "%";
            int textWidth = client.textRenderer.getWidth(Text.literal(text));
            int x = (client.getWindow().getScaledWidth() - textWidth) / 2;
            int y = 10;

            drawContext.drawText(client.textRenderer, Text.literal(text), x, y, 0xFFFFFF, true);
        }
    }
}