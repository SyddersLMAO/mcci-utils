package com.sydders.mcciutils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = MCCIUtils.MOD_ID)
public class MCCIUtilsModConfig implements ConfigData {
    public boolean fishHud = true;
    public int fishHudFadeTime = 5000;

    public boolean treasureOutput = false;

    public int instance = 8;
    public boolean autoFishtance = false;
}
