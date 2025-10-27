//package net.mrrites.fnafmod.block.entity;
//
//import me.srrapero720.waterframes.common.block.DisplayBlock;
//import me.srrapero720.waterframes.common.network.packets.PausePacket;
//import net.irisshaders.iris.Iris;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import foundry.veil.api.client.render.VeilRenderSystem;
//import foundry.veil.api.client.render.light.data.AreaLightData;
//import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
//import foundry.veil.api.client.render.light.renderer.LightRenderer;
//import net.irisshaders.iris.api.v0.IrisApi;
//import net.mrrites.fnafmod.FNAFMOD;
//import me.srrapero720.waterframes.common.network.DisplayNetwork;
//
//
//
//public abstract class BaseVeilLightBlockEntity extends BlockEntity {
//    protected LightRenderHandle<AreaLightData> lightHandle;
//
//    public BaseVeilLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    /** Настройки света — реализуй в потомках */
//    protected abstract void configureLight(AreaLightData light);
//
//    /** true — обновлять свет каждый тик. false — только при создании */
//    public boolean shouldUpdateLightEveryTick() {
//        return false;
//    }
//
//    /** Создать/обновить источник света */
//    public void updateLight() {
//        if (lightHandle == null) {
//            LightRenderer renderer = VeilRenderSystem.renderer().getLightRenderer();
//            lightHandle = renderer.addLight(new AreaLightData());
//        }
//        AreaLightData light = lightHandle.getLightData();
//        configureLight(light);
//        //ТУТ добавим установку паузы
////        BlockPos blockPos1 = new BlockPos(-15, 20, -54); // x=100, y=64, z=200
////        DisplayNetwork.sendServer(new PausePacket(blockPos1, true, 60, true));
//        //int tick = display.tile.data.tick;
//        FNAFMOD.LOGGER.info("PausePacket");
////        IrisApi.getInstance().getConfig().setShadersEnabledAndApply(false);
////        FNAFMOD.LOGGER.info("SHADERS DISABLED");
//    }
//
//    /** Тикер для BlockEntity */
//    public static <T extends BaseVeilLightBlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T be) {
//        if (!world.isClient) return;
//        if (be.lightHandle == null) {
//            be.updateLight();
//        } else if (be.shouldUpdateLightEveryTick()) {
//            be.updateLight();
//        }
//    }
//
//    @Override
//    public void markRemoved() {
//        super.markRemoved();
//        if (lightHandle != null) {
//            lightHandle.free();
//            lightHandle = null;
//            //ТУТ продолжим воспроизведение видео с того же момента
////            BlockPos blockPos1 = new BlockPos(-15, 20, -54); // x=100, y=64, z=200
////            DisplayNetwork.sendServer(new PausePacket(blockPos1, false, 60, true));
////            IrisApi.getInstance().getConfig().setShadersEnabledAndApply(true);
////            FNAFMOD.LOGGER.info("SHADERS ENABLED");
////            FNAFMOD.LOGGER.info(String.valueOf(Iris.getIrisConfig().getShaderPackName()));
//        }
//    }
//}



package net.mrrites.fnafmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.api.client.render.light.renderer.LightRenderer;

// Импорт Waterframes
import me.srrapero720.waterframes.common.block.entity.DisplayTile;
import me.srrapero720.waterframes.common.network.DisplayNetwork;
import me.srrapero720.waterframes.common.network.packets.PausePacket;
import net.mrrites.fnafmod.FNAFMOD;

public abstract class BaseVeilLightBlockEntity extends BlockEntity {
    protected LightRenderHandle<AreaLightData> lightHandle;

    public BaseVeilLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /** Настройки света — реализуй в потомках */
    protected abstract void configureLight(AreaLightData light);

    /** true — обновлять свет каждый тик. false — только при создании */
    public boolean shouldUpdateLightEveryTick() {
        return false;
    }

    /** Создать/обновить источник света */
    public void updateLight() {
        if (lightHandle == null) {
            LightRenderer renderer = VeilRenderSystem.renderer().getLightRenderer();
            lightHandle = renderer.addLight(new AreaLightData());
        }
        AreaLightData light = lightHandle.getLightData();
        configureLight(light);
        World world = this.getWorld(); // или передайте world из параметра класса/метода
        BlockPos displayBlockPos = new BlockPos(-15, 20, -54); // замените на актуальные координаты
        this.setWaterframesPause(world, displayBlockPos);
    }

    /** Тикер для BlockEntity */
    public static <T extends BaseVeilLightBlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T be) {
        if (!world.isClient) return;
        if (be.lightHandle == null) {
            be.updateLight();
        } else if (be.shouldUpdateLightEveryTick()) {
            be.updateLight();
        }
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (lightHandle != null) {
            lightHandle.free();
            lightHandle = null;
            World world = this.getWorld(); // или передайте world из параметра класса/метода
            BlockPos displayBlockPos = new BlockPos(-15, 20, -54); // замените на актуальные координаты
            this.resumeWaterframesPlayback(world, displayBlockPos);
        }
    }

    // --- ВСТАВЬТЕ ЭТИ МЕТОДЫ В НУЖНОЕ МЕСТО ---

    /** Установить паузу для Waterframes дисплея */
    public void setWaterframesPause(World world, BlockPos displayPos) {
        FNAFMOD.LOGGER.info("setWaterframesPause");
        //if (world.isClient) return; // На сервере!
        BlockEntity entity = world.getBlockEntity(displayPos);
        if (entity instanceof DisplayTile displayTile) {
            int tick = displayTile.data.tick; // текущий тик

            DisplayNetwork.sendServer(new PausePacket(displayPos, true, tick, true));
        }
    }

    /** Продолжить воспроизведение видео Waterframes */
    public void resumeWaterframesPlayback(World world, BlockPos displayPos) {
        FNAFMOD.LOGGER.info("resumeWaterframesPlayback");
        //if (world.isClient) return; // На сервере!
        BlockEntity entity = world.getBlockEntity(displayPos);
        if (entity instanceof DisplayTile displayTile) {
            int tick = displayTile.data.tick; // текущий тик
            FNAFMOD.LOGGER.info("Paused Disable");
            DisplayNetwork.sendServer(new PausePacket(displayPos, false, tick, true));
        }
    }
}