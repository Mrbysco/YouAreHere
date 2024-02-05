package com.mrbysco.youarehere.util;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.condition.PlaceType;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.YLevelPlace;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = YouAreHere.MOD_ID)
public class PlaceUtil {
	private static final Map<ResourceLocation, BiomePlace> biomePlaces = new HashMap<>();
	private static final Map<ResourceLocation, DimensionPlace> dimensionPlaces = new HashMap<>();
	private static final Map<ResourceLocation, YLevelPlace> yLevelPlaces = new HashMap<>();

	public static Map<ResourceLocation, ? extends BasePlace> getPlaces(PlaceType type) {
		return switch (type) {
			case BIOME -> biomePlaces;
			case DIMENSION -> dimensionPlaces;
			case Y_LEVEL -> yLevelPlaces;
		};
	}

	public static BasePlace getPlace(PlaceType type, ResourceLocation id) {
		if(id.getPath().equals("y_below_zero")) {
			System.out.println(type + " " + id);
		}
		return switch (type) {
			case BIOME -> biomePlaces.get(id);
			case DIMENSION -> dimensionPlaces.get(id);
			case Y_LEVEL -> yLevelPlaces.get(id);
		};
	}

	@SubscribeEvent
	public static void onTagsUpdated(OnDatapackSyncEvent event) {
		final RegistryAccess registryAccess = event.getPlayerList().getServer().registryAccess();

		biomePlaces.clear();
		final Registry<BiomePlace> biomePlaceRegistry = registryAccess.registryOrThrow(BiomePlace.REGISTRY_KEY);
		biomePlaceRegistry.entrySet().forEach((key) -> biomePlaces.put(key.getKey().location(), key.getValue()));
		YouAreHere.LOGGER.info("Loaded Biome Places: " + biomePlaces.size() + " places");

		dimensionPlaces.clear();
		final Registry<DimensionPlace> dimensionPlaceRegistry = registryAccess.registryOrThrow(DimensionPlace.REGISTRY_KEY);
		dimensionPlaceRegistry.entrySet().forEach((key) -> dimensionPlaces.put(key.getKey().location(), key.getValue()));
		YouAreHere.LOGGER.info("Loaded Dimension Places: " + dimensionPlaces.size() + " places");

		yLevelPlaces.clear();
		final Registry<YLevelPlace> yLevellaceRegistry = registryAccess.registryOrThrow(YLevelPlace.REGISTRY_KEY);
		yLevellaceRegistry.entrySet().forEach((key) -> yLevelPlaces.put(key.getKey().location(), key.getValue()));
		YouAreHere.LOGGER.info("Loaded Y Level Places: " + yLevelPlaces.size() + " places");
	}
}
