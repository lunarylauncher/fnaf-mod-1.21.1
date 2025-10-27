//package net.mrrites.fnafmod.block.custom;
//
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.BlockWithEntity;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.block.entity.BlockEntityTicker;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.mrrites.fnafmod.block.entity.custom.CeilingLightEntity;
//import org.jetbrains.annotations.Nullable;
//
//public class CeilingLight extends BlockWithEntity {
//
//    public static final MapCodec<CeilingLight> CODEC = CeilingLight.createCodec(CeilingLight::new);
//
//    public CeilingLight(Settings settings) {
//        super(settings);
//    }
//
//    @Override
//    protected MapCodec<? extends BlockWithEntity> getCodec() {
//        return CODEC;
//    }
//
//    @Override
//    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new CeilingLightEntity(pos, state);
//    }
//
//
//    @Override
//    protected BlockRenderType getRenderType(BlockState state) {
//        return BlockRenderType.MODEL;
//    }
//}


//package net.mrrites.fnafmod.block.custom;
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.BlockWithEntity;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.block.entity.BlockEntityTicker;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.mrrites.fnafmod.block.entity.custom.CeilingLightEntity;
//import org.jetbrains.annotations.Nullable;
//
//public class CeilingLight extends BlockWithEntity {
//
//    public static final MapCodec<CeilingLight> CODEC = CeilingLight.createCodec(CeilingLight::new);
//
//    public CeilingLight(Settings settings) {
//        super(settings);
//    }
//
//    @Override
//    protected MapCodec<? extends BlockWithEntity> getCodec() {
//        return CODEC;
//    }
//
//    @Override
//    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new CeilingLightEntity(pos, state);
//    }
//
//    @Override
//    protected BlockRenderType getRenderType(BlockState state) {
//        return BlockRenderType.MODEL;
//    }
//
//    // Добавь этот метод, чтобы тикер работал
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        if (world.isClient) {
//            return (w, pos, st, be) -> {
//                if (be instanceof CeilingLightEntity cle) {
//                    CeilingLightEntity.clientTick(w, pos, st, cle);
//                }
//            };
//        }
//        return null;
//    }
//}


package net.mrrites.fnafmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.mrrites.fnafmod.block.BaseVeilLightBlock;
import net.mrrites.fnafmod.block.entity.custom.CeilingLightEntity;
import org.jetbrains.annotations.Nullable;

public class CeilingLight extends BaseVeilLightBlock {
    public static final MapCodec<CeilingLight> CODEC = createCodec(CeilingLight::new);

    public CeilingLight(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends CeilingLight> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CeilingLightEntity(pos, state);
    }
}