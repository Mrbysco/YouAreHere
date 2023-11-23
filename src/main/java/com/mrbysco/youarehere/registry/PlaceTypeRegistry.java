package com.mrbysco.youarehere.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.condition.EnableBiomePlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableDimensionPlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableYLevelPlaces;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.YLevelPlace;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class PlaceTypeRegistry {
	public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(BiomePlace.REGISTRY_KEY,
				BiomePlace.DIRECT_CODEC, BiomePlace.DIRECT_CODEC);
		event.dataPackRegistry(DimensionPlace.REGISTRY_KEY,
				DimensionPlace.DIRECT_CODEC, DimensionPlace.DIRECT_CODEC);
		event.dataPackRegistry(YLevelPlace.REGISTRY_KEY,
				YLevelPlace.DIRECT_CODEC, YLevelPlace.DIRECT_CODEC);
		YouAreHere.LOGGER.info("Registered place type registries");
	}

	public static final DeferredRegister<Codec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, YouAreHere.MOD_ID);
	public static final DeferredHolder<Codec<? extends ICondition>, Codec<EnableBiomePlacesCondition>> DEFAULT_BIOME_ENABLED = CONDITION_CODECS.register("enable_biome_places", () -> EnableBiomePlacesCondition.CODEC);
	public static final DeferredHolder<Codec<? extends ICondition>, Codec<EnableDimensionPlacesCondition>> DEFAULT_DIMENSION_ENABLED = CONDITION_CODECS.register("enable_dimension_places", () -> EnableDimensionPlacesCondition.CODEC);
	public static final DeferredHolder<Codec<? extends ICondition>, Codec<EnableYLevelPlaces>> DEFAULT_Y_LEVEL_ENABLED = CONDITION_CODECS.register("enable_y_level_places", () -> EnableYLevelPlaces.CODEC);

}
