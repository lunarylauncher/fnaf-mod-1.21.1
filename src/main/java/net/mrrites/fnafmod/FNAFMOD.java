package net.mrrites.fnafmod;

import net.fabricmc.api.ModInitializer;

import net.mrrites.fnafmod.init.ModBlocks;
import net.mrrites.fnafmod.init.ModBlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FNAFMOD implements ModInitializer {
	public static final String MOD_ID = "fnafmod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModBlockEntity.registerBlockEntities();
		//GlobalServerTickWatcher.register();
		LOGGER.info("Hello Fabric world!");

	}
}
