package com.mrbysco.youarehere.network.packet;

import com.mrbysco.youarehere.registry.condition.PlaceType;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.util.PlaceUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent.Context;

public class ShowTitleMessage {
	private final ResourceLocation place;
	private final String type;

	public ShowTitleMessage(ResourceLocation place, String type) {
		this.place = place;
		this.type = type;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.place);
		buffer.writeUtf(this.type);
	}

	public static ShowTitleMessage decode(final FriendlyByteBuf buffer) {
		return new ShowTitleMessage(buffer.readResourceLocation(), buffer.readUtf());
	}

	public void handle(Context ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				BasePlace place = PlaceUtil.getPlace(PlaceType.getFromName(this.type), this.place);
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
			}
		});
		ctx.setPacketHandled(true);
	}
}
