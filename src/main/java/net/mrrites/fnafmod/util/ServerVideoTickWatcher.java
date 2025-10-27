package net.mrrites.fnafmod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.block.entity.BlockEntity;
import me.srrapero720.waterframes.common.block.entity.DisplayTile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.math.random.Random;


public class ServerVideoTickWatcher {
    private final BlockPos displayPos;
    private final int targetTick;
    private boolean triggered = false;

    public ServerVideoTickWatcher(BlockPos displayPos, int targetSeconds) {
        this.displayPos = displayPos;
        this.targetTick = targetSeconds;
    }

    public void check(ServerWorld serverWorld) {
        BlockEntity blockEntity = serverWorld.getBlockEntity(displayPos);
        if (!(blockEntity instanceof DisplayTile displayTile)) return;
        int currentTick = displayTile.data.tick;

        // Сброс триггера если видео сброшено на начало (tick < targetTick)
        if (triggered && currentTick < targetTick) {
            triggered = false;
        }

        if (!triggered && currentTick >= targetTick) {
            triggered = true;
            Random random = serverWorld.random;
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                Vec3d playerPos = player.getPos();

                for (int i = 0; i < 10; i++) {
                    double angle = random.nextDouble() * 2 * Math.PI;
                    double distance = 2 + random.nextDouble() * 3;
                    double x = playerPos.x + Math.cos(angle) * distance;
                    double y = playerPos.y + 2 + random.nextDouble() * 2; // немного выше игрока
                    double z = playerPos.z + Math.sin(angle) * distance;

                    // Летучая мышь
                    BatEntity bat = EntityType.BAT.create(serverWorld);
                    if (bat == null) continue;
                    bat.refreshPositionAndAngles(x, y, z, random.nextFloat() * 360, 0);
                    bat.setInvisible(true);
                    serverWorld.spawnEntity(bat);

                    // Свинья
                    PigEntity pig = EntityType.PIG.create(serverWorld);
                    if (pig == null) continue;
                    pig.refreshPositionAndAngles(x, y, z, random.nextFloat() * 360, 0);
                    serverWorld.spawnEntity(pig);

                    // Скелет-иссушитель
                    WitherSkeletonEntity skeleton = EntityType.WITHER_SKELETON.create(serverWorld);
                    if (skeleton == null) continue;
                    skeleton.refreshPositionAndAngles(x, y, z, random.nextFloat() * 360, 0);
                    serverWorld.spawnEntity(skeleton);

                    // Свинья едет на летучей мыши, скелет едет на свинье
                    pig.startRiding(bat, true);
                    skeleton.startRiding(pig, true);
                }
            }
        }
    }
}