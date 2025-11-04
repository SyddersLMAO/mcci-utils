package com.sydders.mcciutils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = MCCIUtils.MOD_ID)
public class MCCIUtilsModConfig implements ConfigData {
    public boolean fishHud = true;
    public int fishHudFadeTime = 5000;

    public int instance = 8;
    public boolean autoFishtance = false;

    public boolean gameOverMessageToggle = true;
    public String gameOverMessage = "GG";
}
