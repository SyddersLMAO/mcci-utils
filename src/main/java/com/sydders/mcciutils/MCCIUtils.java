package com.sydders.mcciutils;

import net.fabricmc.api.ModInitializer;

import net.minecraft.text.MutableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCCIUtils implements ModInitializer {
	public static final String MOD_ID = "mcci-utils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}

	public static class ChatMessageStorage {
		public static MutableText latestMessage = null;
		public static long lastUpdatedTime = 0;

		public static void setMessage(MutableText msg) {
			latestMessage = msg;
			lastUpdatedTime = System.currentTimeMillis();
		}
	}

}