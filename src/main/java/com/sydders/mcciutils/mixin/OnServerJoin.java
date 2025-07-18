package com.sydders.mcciutils.mixin;

import com.sydders.mcciutils.MCCIUtils;
import com.sydders.mcciutils.MCCIUtilsModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPlayNetworkHandler.class)
public class OnServerJoin {
    MCCIUtilsModConfig config = AutoConfig.getConfigHolder(MCCIUtilsModConfig.class).getConfig();

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onServerJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (!config.autoFishtance) return;

        MinecraftClient client = MinecraftClient.getInstance();

        Text footer = ((PlayerListHudAccessor) client.inGameHud.getPlayerListHud()).getFooter();
        if (footer == null) return;

        String footerText = footer.getString();
        Pattern pattern = Pattern.compile("INSTANCE (\\d+)");
        Matcher matcher = pattern.matcher(footerText);

        MCCIUtils.LOGGER.info(footerText);

        if (matcher.find()) {
            int currentInstance = Integer.parseInt(matcher.group(1));
            if (currentInstance != config.instance) {
                client.player.networkHandler.sendChatCommand("instance join " + config.instance);
            }
        } else {
            client.player.networkHandler.sendChatCommand("instance join " + config.instance);
        }
    }
}
