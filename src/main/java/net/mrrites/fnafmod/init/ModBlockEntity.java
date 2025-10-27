package net.mrrites.fnafmod.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mrrites.fnafmod.FNAFMOD;
import net.mrrites.fnafmod.block.entity.custom.CeilingLightEntity;

public class ModBlockEntity {
    public static final BlockEntityType<CeilingLightEntity> CEILING_LIGHT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(FNAFMOD.MOD_ID, "ceiling_light_be"),
                    BlockEntityType.Builder.create(CeilingLightEntity::new, ModBlocks.CEILING_LIGHT).build(null));

    public static void registerBlockEntities() {
        FNAFMOD.LOGGER.info("Registering block entities");
    }
}

