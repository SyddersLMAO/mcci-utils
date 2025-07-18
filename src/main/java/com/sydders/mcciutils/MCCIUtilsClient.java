package com.sydders.mcciutils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;

public class MCCIUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(((drawContext, renderTickCounter) -> {
            MutableText msg = MCCIUtils.ChatMessageStorage.latestMessage;
            if (msg != null) {
                var textRenderer = MinecraftClient.getInstance().textRenderer;

                int textWidth = textRenderer.getWidth(msg);
                int textHeight = textRenderer.fontHeight;

                int x = 2;
                int y = 3;

                drawContext.fill(x - 2, y - 2, x + textWidth + 2, y + textHeight + 2, 0x80000000);

                drawContext.drawText(textRenderer, msg, x, y, 0xFFFFFFFF, false);
            }
        }));
    }
}
