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
        }
    }
}