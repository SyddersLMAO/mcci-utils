package com.sydders.mcciutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.MutableComponent;

public class FishingHud {
    static void extract(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        MutableComponent msg = MCCIUtils.ChatMessageStorage.latestMessage;
        int fadeTimer = Config.HANDLER.instance().fishingHudFadeTimer * 1000;
        long elapsed = System.currentTimeMillis() - MCCIUtils.ChatMessageStorage.lastUpdatedTime;
        if (msg != null && elapsed < fadeTimer) {
            float opacity = 1.0f - ((float) elapsed / fadeTimer);
            int alpha = (int)(opacity * 255) << 24;
            int colour = alpha | 0x00FFFFFF;
            int backgroundColour = 0x80000000;

            Font font = Minecraft.getInstance().font;
            int textWidth = font.width(msg);


            FishingHudPosition pos = Config.HANDLER.instance().fishingHudPosition;
            int x = switch (pos) {
                case TOP_LEFT, BOTTOM_LEFT -> 3;
                case TOP_RIGHT, BOTTOM_RIGHT -> graphics.guiWidth() - textWidth - 3;
            };
            int y = switch (pos) {
                case TOP_LEFT, TOP_RIGHT -> 3;
                case BOTTOM_LEFT, BOTTOM_RIGHT -> graphics.guiHeight() - 12;
            };

            graphics.fill(x - 2, y - 2, x + font.width(msg) + 2, y + 9, backgroundColour);
            graphics.text(font, msg, x, y, colour, false);
        }
    }
}