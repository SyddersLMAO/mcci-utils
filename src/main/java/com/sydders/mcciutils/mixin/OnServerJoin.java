package com.sydders.mcciutils.mixin;

import com.sydders.mcciutils.Config;
import com.sydders.mcciutils.MCCIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPacketListener.class)
public class OnServerJoin {


    @Inject(method = "handleLogin", at = @At("HEAD"))
    private void onServerJoin(ClientboundLoginPacket packet, CallbackInfo ci) {
        boolean autoInstanceToggle = Config.HANDLER.instance().autoInstance;
        int autoInstanceNumber = Config.HANDLER.instance().autoInstanceNumber;
        if (!autoInstanceToggle) return;

        Minecraft client = Minecraft.getInstance();
        Component footer = ((PlayerTabOverlayAccessor) client.gui.getTabList()).getFooter();
        if (footer == null) return;

        String footerText = footer.getString();
        Pattern pattern = Pattern.compile("Instance (\\d+)");
        Matcher matcher = pattern.matcher(footerText);

        if (matcher.find()) {
            int currentInstance = Integer.parseInt(matcher.group(1));
            if (currentInstance != autoInstanceNumber) {
                client.player.connection.sendCommand("instance join " + autoInstanceNumber);
            }
        } else {
            client.player.connection.sendCommand("instance join " + autoInstanceNumber);
        }
    }
}
