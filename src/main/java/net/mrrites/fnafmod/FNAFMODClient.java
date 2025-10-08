//package net.mrrites.fnafmod;
//
//import net.fabricmc.api.ClientModInitializer;
//
//
//public class FNAFMODClient implements ClientModInitializer {
//    @Override
//    public void onInitializeClient() {
//
//
//    }
//}
package net.mrrites.fnafmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FNAFMODClient implements ClientModInitializer {
    private static final Map<UUID, Boolean> previousShaderState = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ShaderPayload.registerClient((client, enable) -> {
            UUID playerId = client.player.getUuid();
            client.execute(() -> {
                var config = net.irisshaders.iris.api.v0.IrisApi.getInstance().getConfig();
                if (enable) {
                    Boolean prevEnabled = previousShaderState.get(playerId);
                    if (prevEnabled != null && prevEnabled) {
                        config.setShadersEnabledAndApply(true);
                    }
                } else {
                    previousShaderState.put(playerId, config.areShadersEnabled());
                    config.setShadersEnabledAndApply(false);
                }
            });
        });
    }
}