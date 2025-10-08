//package net.mrrites.fnafmod;
//
//import net.fabricmc.api.ModInitializer;
//
//import net.mrrites.fnafmod.block.ModBlocks;
//import net.mrrites.fnafmod.block.entity.ModBlockEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class FNAFMOD implements ModInitializer {
//	public static final String MOD_ID = "fnafmod";
//
//	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//
//	@Override
//	public void onInitialize() {
//		ModBlocks.registerModBlocks();
//		ModBlockEntity.registerBlockEntities();
//		LOGGER.info("Hello Fabric world!");
//
//	}
//}

package net.mrrites.fnafmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mrrites.fnafmod.block.ModBlocks;
import net.mrrites.fnafmod.block.entity.ModBlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FNAFMOD implements ModInitializer {
	public static final String MOD_ID = "fnafmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModBlockEntity.registerBlockEntities();
		LOGGER.info("Hello Fabric world!");

		// Регистрация команды для управления шейдерами
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ShaderAdminManager.registerCommand(dispatcher);
		});
	}
}