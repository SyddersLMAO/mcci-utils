package com.sydders.mcciutils.mixin;

import com.sydders.mcciutils.Config;
import com.sydders.mcciutils.MCCIUtils;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPacketListener.class)
public class PlayerChatMessages {

	@Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
	private void onGameMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
		boolean fishHudToggle = Config.HANDLER.instance().fishHud;
		if (!fishHudToggle) { return; }

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

				Pattern multiplierPattern = Pattern.compile("x(\\d+)");
				Matcher multiplierMatcher = multiplierPattern.matcher(msg);
				String multiplier = multiplierMatcher.find() ? " x" + multiplierMatcher.group(1) : "";

				MutableComponent iconComponent = Component.literal(iconText.toString())
						.withStyle(Style.EMPTY.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath("mcc", "icon"))));
				MutableComponent nameComponent = Component.literal(normalText.toString().trim() + multiplier);

				MCCIUtils.ChatMessageStorage.setLatestMessage(
						Component.empty().append(iconComponent).append(nameComponent)
				);
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
				MutableComponent iconComponent = Component.literal(iconText.toString())
						.withStyle(Style.EMPTY.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath("mcc", "icon"))));

				MutableComponent current = MCCIUtils.ChatMessageStorage.latestMessage;
				if (current == null) current = Component.empty();

				MCCIUtils.ChatMessageStorage.setLatestMessage(
						Component.empty().append(current).append(" ").append(iconComponent)
				);
			}
			ci.cancel();

		} else if (msg.toLowerCase().contains("you earned:")) {
			Pattern pattern = Pattern.compile("You earned: (\\d+)");
			Matcher matcher = pattern.matcher(msg);

			if (matcher.find()) {
				int islandXp = Integer.parseInt(matcher.group(1));
				MutableComponent current = MCCIUtils.ChatMessageStorage.latestMessage;
				if (current != null) {
					MCCIUtils.ChatMessageStorage.setLatestMessage(
							Component.empty().append(current).append(" +" + islandXp + "xp")
					);
				}
			}
			ci.cancel();
		}
	}
}