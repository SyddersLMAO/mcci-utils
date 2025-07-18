package com.sydders.mcciutils.mixin;

import com.sydders.mcciutils.MCCIUtilsModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class OnServerJoin {
    MCCIUtilsModConfig config = AutoConfig.getConfigHolder(MCCIUtilsModConfig.class).getConfig();

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onServerJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (config.autoFishtance) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.networkHandler.sendChatCommand("instance join " + config.instance);
            }
        }
    }
}
