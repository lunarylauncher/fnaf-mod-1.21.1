//package net.mrrites.fnafmod.block;
//
//import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
//import net.minecraft.block.AbstractBlock;
//import net.minecraft.block.Block;
//import net.minecraft.block.Blocks;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroups;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.sound.BlockSoundGroup;
//import net.minecraft.util.Identifier;
//import net.mrrites.fnafmod.FNAFMOD;
//import net.mrrites.fnafmod.block.custom.CeilingLight;
//
//public class ModBlocks {
//
//     public static final Block TEST_BLOCK = registerBlock("test_block",
//             new Block(AbstractBlock.Settings.create().strength(4f)
//                     .requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)));
//
////    public static final Block CEILING_LIGHT = registerBlock("ceiling_light",
////            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).hardness(-1f).solid().noBlockBreakParticles()));
//public static final Block CEILING_LIGHT = registerBlock("ceiling_light",
//        new CeilingLight(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).hardness(-1f).solid().noBlockBreakParticles()));
//
//    //new CeilingLight(AbstractBlock.Settings.create().strength(4f)));
//
//    private static Block registerBlock(String name, Block block) {
//        registerBlockItem(name, block);
//        return Registry.register(Registries.BLOCK, Identifier.of(FNAFMOD.MOD_ID, name), block);
//    }
//
//    private static void registerBlockItem(String name, Block block) {
//        Registry.register(Registries.ITEM, Identifier.of(FNAFMOD.MOD_ID,name),
//                new BlockItem(block, new Item.Settings()));
//    }
//
//    public static void registerModBlocks() {
//        FNAFMOD.LOGGER.info("Registering Mod Blocks for: " + FNAFMOD.MOD_ID);
//
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
//            fabricItemGroupEntries.add(ModBlocks.TEST_BLOCK);
//        });
//    }
//}


package net.mrrites.fnafmod.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mrrites.fnafmod.FNAFMOD;
import net.mrrites.fnafmod.block.custom.CeilingLight;

public class ModBlocks {

    public static final Block CEILING_LIGHT = registerBlock("ceiling_light",
            new CeilingLight(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).hardness(1.5f).solid().nonOpaque()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(FNAFMOD.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(FNAFMOD.MOD_ID,name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        FNAFMOD.LOGGER.info("Registering Mod Blocks for: " + FNAFMOD.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(ModBlocks.CEILING_LIGHT);
        });
    }
}