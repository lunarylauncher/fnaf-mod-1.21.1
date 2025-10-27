package net.mrrites.fnafmod.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.mrrites.fnafmod.FNAFMOD;

import java.util.ArrayList;
import java.util.List;

public class GlobalServerTickWatcher {
    private static final List<ServerVideoTickWatcher> watchers = new ArrayList<>();

    static {
        watchers.add(new ServerVideoTickWatcher(new BlockPos(-15, 20, -54), 300));
    }

    public static void register() {
//        FNAFMOD.LOGGER.info("register GlobalServerTickWatcher");
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerWorld world = server.getOverworld();
            if (world == null) return;
            for (ServerVideoTickWatcher watcher : watchers) {
//                FNAFMOD.LOGGER.info("TICK GlobalServerTickWatcher");
                watcher.check(world);
            }
        });
    }
}