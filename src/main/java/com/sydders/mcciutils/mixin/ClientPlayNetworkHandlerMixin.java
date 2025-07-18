package com.sydders.mcciutils.mixin;

import com.sydders.mcciutils.MCCIUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        String msg = packet.content().getString();
        if (msg.toLowerCase().contains("you caught")) {
            Pattern pattern = Pattern.compile("\\[(.*?)\\]");
            Matcher matcher = pattern.matcher(msg);

            if (matcher.find()) {
                String fishNameRaw = matcher.group(1);

                StringBuilder normalText = new StringBuilder();
                StringBuilder iconText = new StringBuilder();

                for (char ch : fishNameRaw.toCharArray()) {
                    if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.PRIVATE_USE_AREA) {
                        iconText.append(ch);
                    } else {
                        normalText.append(ch);
                    }
                }

                Text iconComponent = Text.literal(iconText.toString())
                        .setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "icon")));
                Text nameComponent = Text.literal(normalText.toString().trim());

                MCCIUtils.ChatMessageStorage.latestMessage = Text.empty().append(nameComponent).append(iconComponent);

                MCCIUtils.LOGGER.info("Caught fish (text): " + normalText);
                MCCIUtils.LOGGER.info("Caught fish (icons): " + iconText);
            }
            ci.cancel();
        } else if (msg.toLowerCase().contains("triggered:")) {
            StringBuilder iconText = new StringBuilder();
            for (char ch : msg.toCharArray()) {
                if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.PRIVATE_USE_AREA && ch != '\uE018') {
                    iconText.append(ch);
                }
            }

            if (!iconText.isEmpty()) {
                Text iconComponent = Text.literal(iconText.toString())
                        .setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "icon")));

                if (MCCIUtils.ChatMessageStorage.latestMessage == null) {
                    MCCIUtils.ChatMessageStorage.latestMessage = Text.empty();
                }

                MCCIUtils.ChatMessageStorage.latestMessage = Text.empty().append(MCCIUtils.ChatMessageStorage.latestMessage).append(" ").append(iconComponent);

                MCCIUtils.LOGGER.info("Triggered icons: " + iconText);
            }

            ci.cancel();
        } else if (msg.toLowerCase().contains("you earned:")) {
            Pattern pattern = Pattern.compile("You earned: (\\d+)");
            Matcher matcher = pattern.matcher(msg);
            int islandXp = 0;

            if (matcher.find()) {
                islandXp = Integer.parseInt(matcher.group(1));
                System.out.println("Island XP earned: " + islandXp);
            }

            if (islandXp != 0) {
                MCCIUtils.ChatMessageStorage.latestMessage = Text.empty().append(MCCIUtils.ChatMessageStorage.latestMessage).append(" ").append("+" + islandXp + "xp");
            }

            ci.cancel();
        }
    }
}
