package com.mrbysco.youarehere.network.handler;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.payload.ShowTitlePayload;
import com.mrbysco.youarehere.registry.condition.PlaceType;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.util.PlaceUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleData(final ShowTitlePayload payload, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					PlaceType type = PlaceType.getFromName(payload.type());
					BasePlace place = PlaceUtil.getPlace(type, payload.place());
					if (place == null) {
						YouAreHere.LOGGER.error("Failed to find place with id: " + payload.place());
						return;
					}
					net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
					Player player = minecraft.player;
					minecraft.gui.clear();
					minecraft.gui.resetTitleTimes();
					minecraft.gui.setTimes(place.fadeInDuration(), place.duration(), place.fadeOutDuration());
					minecraft.gui.setTitle(Component.translatable(place.title()).withStyle(ChatFormatting.UNDERLINE));
					if (!place.subtitle().isEmpty())
						minecraft.gui.setSubtitle(Component.translatable(place.subtitle()));

					if (place.soundLocation() != null) {
						SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(place.soundLocation());
						if (sound != null) {
							player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), sound, SoundSource.AMBIENT, place.getVolume(), place.getPitch(), false);
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("youarehere.networking.show_title.failed", e.getMessage()));
					return null;
				});
	}
}
