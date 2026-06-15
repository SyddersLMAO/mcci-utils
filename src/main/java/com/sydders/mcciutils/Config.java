package com.sydders.mcciutils;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Identifier.fromNamespaceAndPath(MCCIUtils.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("mcciutils.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean autoInstance = false;

    @SerialEntry
    public int autoInstanceNumber = 8;

    @SerialEntry
    public boolean fishHud = true;

    @SerialEntry
    public FishingHudPosition fishingHudPosition = FishingHudPosition.TOP_LEFT;
}

