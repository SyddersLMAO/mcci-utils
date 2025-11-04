package com.sydders.mcciutils;

import com.sydders.mcciutils.mixin.PlayerListHudAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;

public class MCCIUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(MCCIUtilsModConfig.class, JanksonConfigSerializer::new);
        MCCIUtilsModConfig config = AutoConfig.getConfigHolder(MCCIUtilsModConfig.class).getConfig();

        HudRenderCallback.EVENT.register(((drawContext, renderTickCounter) -> {
            MutableText msg = MCCIUtils.ChatMessageStorage.latestMessage;
            long elapsed = System.currentTimeMillis() - MCCIUtils.ChatMessageStorage.lastUpdatedTime;

            if (msg != null && elapsed < config.fishHudFadeTime) {
                float opacity = 1.0f - ((float) elapsed / config.fishHudFadeTime);
                int alpha = (int)(opacity * 255) << 24;

                var textRenderer = MinecraftClient.getInstance().textRenderer;
                int textWidth = textRenderer.getWidth(msg);
                int textHeight = textRenderer.fontHeight;
                int x = 3;
                int y = 3;

                drawContext.fill(x - 2, y - 2, x + textWidth + 2, y + textHeight + 2, 0x80000000);
                drawContext.drawText(textRenderer, msg, x, y, 0xFFFFFF | alpha, false);
            }
        }));
    }
}
