package com.mrbysco.youarehere.handler;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.payload.ShowTitlePayload;
import com.mrbysco.youarehere.registry.condition.PlaceType;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.util.PlaceUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = YouAreHere.MOD_ID, value = Dist.CLIENT)
public class TitleHandler {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Pre event) {

		if (event.getEntity() instanceof ServerPlayer player) {
			if (player.level().getGameTime() % 40 == 0) {
				CompoundTag persistentData = player.getPersistentData();
				CompoundTag hereData = persistentData.contains("YouAreHere") ? persistentData.getCompoundOrEmpty("YouAreHere") : new CompoundTag();
				if (!PlaceUtil.getPlaces(PlaceType.BIOME).isEmpty()) {
					for (Map.Entry<ResourceLocation, ? extends BasePlace> placeEntry : PlaceUtil.getPlaces(PlaceType.BIOME).entrySet()) {
						checkPlace(player, placeEntry, hereData);
					}
				}
				if (!PlaceUtil.getPlaces(PlaceType.DIMENSION).isEmpty()) {
					for (Map.Entry<ResourceLocation, ? extends BasePlace> placeEntry : PlaceUtil.getPlaces(PlaceType.DIMENSION).entrySet()) {
						checkPlace(player, placeEntry, hereData);
					}
				}
				if (!PlaceUtil.getPlaces(PlaceType.Y_LEVEL).isEmpty()) {
					for (Map.Entry<ResourceLocation, ? extends BasePlace> placeEntry : PlaceUtil.getPlaces(PlaceType.Y_LEVEL).entrySet()) {
						checkPlace(player, placeEntry, hereData);
					}
				}
				persistentData.put("YouAreHere", hereData);
			}
		}
	}

	private static void checkPlace(ServerPlayer player, Map.Entry<ResourceLocation, ? extends BasePlace> entry, CompoundTag hereData) {
		final ResourceLocation id = entry.getKey();
		String idName = entry.toString();
		final BasePlace place = entry.getValue();
		boolean cachedValue = hereData.getBooleanOr(idName, false);
		boolean matches = place.matches(player);
		if (cachedValue) {
			if (!matches) {
				hereData.remove(idName);
			}
		} else {
			if (matches) {
				hereData.putBoolean(idName, true);
				player.connection.send(new ShowTitlePayload(id, place.getType().getName()));
			}
		}
	}
}
