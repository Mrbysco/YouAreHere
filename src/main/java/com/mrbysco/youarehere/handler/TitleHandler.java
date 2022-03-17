package com.mrbysco.youarehere.handler;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.PacketHandler;
import com.mrbysco.youarehere.network.packet.ShowTitleMessage;
import com.mrbysco.youarehere.resources.PlaceManager;
import com.mrbysco.youarehere.resources.places.BasePlace;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Bus.FORGE, modid = YouAreHere.MOD_ID, value = Dist.CLIENT)
public class TitleHandler {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event) {
		if (event.side.isServer()) {
			ServerPlayer player = (ServerPlayer) event.player;
			if (player.level.getGameTime() % 40 == 0 && !PlaceManager.INSTANCE.getPlaces().isEmpty()) {
				CompoundTag persistentData = player.getPersistentData();
				CompoundTag hereData = persistentData.contains("YouAreHere") ? persistentData.getCompound("YouAreHere") : new CompoundTag();
				for (BasePlace place : PlaceManager.INSTANCE.getPlaces()) {
					checkPlace(player, place, hereData);
				}
				persistentData.put("YouAreHere", hereData);
			}
		}
	}

	private static void checkPlace(ServerPlayer player, BasePlace place, CompoundTag hereData) {
		String idName = place.id().toString();
		boolean cachedValue = hereData.getBoolean(idName);
		boolean matches = place.matches(player);
		if (cachedValue) {
			if (!matches) {
				hereData.remove(idName);
			}
		} else {
			if (matches) {
				hereData.putBoolean(idName, true);
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ShowTitleMessage(place.id()));
			}
		}
	}
}
