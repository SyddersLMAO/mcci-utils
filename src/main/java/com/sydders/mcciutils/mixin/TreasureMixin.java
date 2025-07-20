package com.sydders.mcciutils.mixin;

import com.sydders.mcciutils.MCCIUtils;
import com.sydders.mcciutils.MCCIUtilsModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class TreasureMixin {
    MCCIUtilsModConfig config = AutoConfig.getConfigHolder(MCCIUtilsModConfig.class).getConfig();

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        String msg = packet.content().getString();

        if (!config.treasureOutput) {
            String plain = msg.replaceAll("§.", "").replaceAll("[^\\x00-\\x7F]", "").toLowerCase();

            if (plain.contains("you receive:")) {
                ci.cancel();
            }
        }
    }
}
