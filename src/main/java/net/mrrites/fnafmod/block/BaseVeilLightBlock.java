package net.mrrites.fnafmod.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.mrrites.fnafmod.block.entity.BaseVeilLightBlockEntity;

import com.mojang.serialization.MapCodec;

public abstract class BaseVeilLightBlock extends BlockWithEntity {
    protected BaseVeilLightBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return (w, pos, st, be) -> {
                if (be instanceof BaseVeilLightBlockEntity lightBE) {
                    BaseVeilLightBlockEntity.clientTick(w, pos, st, lightBE);
                }
            };
        }
        return null;
    }

    // Требуется во всех наследниках!
    @Override
    protected abstract MapCodec<? extends BlockWithEntity> getCodec();
}