//package net.mrrites.fnafmod.block.entity.custom;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.util.math.BlockPos;
//import net.mrrites.fnafmod.init.ModBlockEntity;
//
//public class CeilingLightEntity extends BlockEntity {
////    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
//
//
//    public CeilingLightEntity(BlockPos pos, BlockState state) {
//        super(ModBlockEntity.CEILING_LIGHT_BE, pos, state);
//    }
//}


//package net.mrrites.fnafmod.block.entity.custom;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.mrrites.fnafmod.init.ModBlockEntity;
//import foundry.veil.api.client.render.VeilRenderSystem;
//import foundry.veil.api.client.render.light.data.AreaLightData;
//import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
//import foundry.veil.api.client.render.light.renderer.LightRenderer;
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//
//public class CeilingLightEntity extends BlockEntity {
//    private LightRenderHandle<AreaLightData> lightHandle;
//
//    public CeilingLightEntity(BlockPos pos, BlockState state) {
//        super(ModBlockEntity.CEILING_LIGHT_BE, pos, state);
//    }
//
//    public static void clientTick(World world, BlockPos pos, BlockState state, CeilingLightEntity be) {
//        if (!world.isClient) return;
//
//        // Отладка!
//        System.out.println("clientTick called for CeilingLightEntity at " + pos);
//
//        if (be.lightHandle == null) {
//            System.out.println("Creating lightHandle for CeilingLightEntity at " + pos);
//            LightRenderer lightRenderer = VeilRenderSystem.renderer().getLightRenderer();
//            be.lightHandle = lightRenderer.addLight(new AreaLightData());
//        }
//
//        AreaLightData light = be.lightHandle.getLightData();
//        Vec3d position = pos.toCenterPos();
//        light.getPosition().set((float)position.x, (float)position.y, (float)position.z);
//        light.getOrientation().set(new Quaternionf().rotateXYZ((float)Math.toRadians(-90), 0, 0));
//        light.setDistance(30f);
//        light.setAngle(1f);
//        light.setSize(1f, 1f);
//        light.setBrightness(10f);
//        light.setColor(new Vector3f(1f, 1f, 0.85f));
//    }
//
//    @Override
//    public void markRemoved() {
//        super.markRemoved();
//        if (lightHandle != null) {
//            lightHandle.free();
//            lightHandle = null;
//        }
//    }
//}

package net.mrrites.fnafmod.block.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.mrrites.fnafmod.block.entity.BaseVeilLightBlockEntity;
import net.mrrites.fnafmod.init.ModBlockEntity;
import foundry.veil.api.client.render.light.data.AreaLightData;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.minecraft.util.math.Vec3d;

public class CeilingLightEntity extends BaseVeilLightBlockEntity {
    public CeilingLightEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.CEILING_LIGHT_BE, pos, state);
    }

    @Override
    protected void configureLight(AreaLightData light) {
        Vec3d position = pos.toCenterPos().add(-0.5, -0.249, 0);
        light.getPosition().set((float) position.x, (float) position.y, (float) position.z);
        light.getOrientation().set(new Quaternionf().rotateXYZ((float) Math.toRadians(-90), 0, 0));
        light.setDistance(10.0f);
        light.setAngle(0.99f);
        light.setSize(1.0f, 0.103f);
        light.setBrightness(2.6f);
        light.setColor(new Vector3f(1f, 1f, 0.85f));
    }

    @Override
    public boolean shouldUpdateLightEveryTick() {
        return false;
    }
}

