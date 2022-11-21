package com.mrbysco.youarehere.network.packet;

import com.mrbysco.youarehere.resources.PlaceManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.Serial;
import java.util.function.Supplier;

public class ShowTitleMessage {
	private final ResourceLocation place;

	public ShowTitleMessage(ResourceLocation place) {
		this.place = place;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.place);
	}

	public static ShowTitleMessage decode(final FriendlyByteBuf buffer) {
		return new ShowTitleMessage(buffer.readResourceLocation());
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				ShowTitle.update(this.place).run();
			}
		});
		ctx.setPacketHandled(true);
	}

	private static class ShowTitle {
		private static SafeRunnable update(ResourceLocation place) {
			return new SafeRunnable() {
				@Serial
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					PlaceManager.INSTANCE.byKey(place).ifPresent(place -> {
						net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
						Player player = minecraft.player;
						minecraft.gui.clear();
						minecraft.gui.resetTitleTimes();
						minecraft.gui.setTimes(place.fadeInDuration(), place.duration(), place.fadeOutDuration());
						minecraft.gui.setTitle(Component.translatable(place.title()).withStyle(ChatFormatting.UNDERLINE));
						if (!place.subtitle().isEmpty())
							minecraft.gui.setSubtitle(Component.translatable(place.subtitle()));

						if (place.soundLocation() != null) {
							SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(place.soundLocation());
							if (sound != null) {
								player.level.playLocalSound(player.getX(), player.getY(), player.getZ(), sound, SoundSource.AMBIENT, place.getVolume(), place.getPitch(), false);
							}
						}
					});
				}
			};
		}
	}
}
