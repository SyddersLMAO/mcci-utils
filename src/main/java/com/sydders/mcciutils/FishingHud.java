package com.sydders.mcciutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.MutableComponent;

public class FishingHud {
    static void extract(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        MutableComponent msg = MCCIUtils.ChatMessageStorage.latestMessage;
        long elapsed = System.currentTimeMillis() - MCCIUtils.ChatMessageStorage.lastUpdatedTime;
        if (msg != null && elapsed < 2000) {
            float opacity = 1.0f - ((float) elapsed / 2000);
            int alpha = (int)(opacity * 255) << 24;
            int color = alpha | 0x00FFFFFF;

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

            graphics.fill(x - 2, y - 2, x + font.width(msg) + 2, y + 9, 0x80000000);
            graphics.text(font, msg, x, y, color, false);
        }
    }
}