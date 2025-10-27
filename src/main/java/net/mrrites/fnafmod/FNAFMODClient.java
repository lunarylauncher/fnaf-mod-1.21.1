package net.mrrites.fnafmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.mrrites.fnafmod.client.DownloadManager;
import net.mrrites.fnafmod.client.HudOverlay;

public class FNAFMODClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        HudOverlay.register();

        // При заходе на сервер — стартуем загрузку (или возобновляем)
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            DownloadManager.getInstance().startDownloadIfNeeded();
        });

        // При отключении — останавливаем загрузку (сохранится .part)
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            DownloadManager.getInstance().pauseDownload();
        });

    }
}

