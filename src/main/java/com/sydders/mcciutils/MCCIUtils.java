package com.sydders.mcciutils;

import net.fabricmc.api.ModInitializer;

import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCCIUtils implements ModInitializer {
	public static final String MOD_ID = "mcciutils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Config.HANDLER.load();

		LOGGER.info("MCCI Utils has been initialised");
	}

	public static class ChatMessageStorage {
		public static MutableComponent latestMessage = null;
		public static long lastUpdatedTime = 0;

		public static void setLatestMessage(MutableComponent message) {
			latestMessage = message;
			lastUpdatedTime = System.currentTimeMillis();
		}
	}
}