package com.mrbysco.youarehere.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.condition.ConfigEnabledCondition;
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
	public static final DeferredHolder<Codec<? extends ICondition>, Codec<ConfigEnabledCondition>> CONFIG_ENABLED = CONDITION_CODECS.register("config_enabled", () -> ConfigEnabledCondition.CODEC);
}
