package com.mrbysco.youarehere.datagen.server;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.PlaceProvider;
import com.mrbysco.youarehere.registry.condition.ConfigDefault;
import com.mrbysco.youarehere.registry.condition.ConfigEnabledCondition;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.YLevelPlace;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModPlaceProvider extends PlaceProvider {
	public ModPlaceProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider, YouAreHere.MOD_ID);
	}

	@Override
	protected void start(HolderLookup.Provider registries) {
		registerBiomes(registries);

		addDimensionPlace("dimension_the_nether", new DimensionPlace(
						Identifier.withDefaultNamespace("the_nether"),
						SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location(), 1.0F, 1.0F,
						"youarehere.dimension.the_nether", "",
						20, 20, 20),
				new ConfigEnabledCondition(ConfigDefault.DIMENSION));
		addDimensionPlace("dimension_the_end", new DimensionPlace(
						Identifier.withDefaultNamespace("the_end"),
						SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location(), 1.0F, 1.0F,
						"youarehere.dimension.the_end", "",
						20, 20, 20),
				new ConfigEnabledCondition(ConfigDefault.DIMENSION));

		addYLevelPlace("y_below_zero", new YLevelPlace(-64, 0,
						Identifier.withDefaultNamespace("overworld"),
						SoundEvents.LAVA_POP.location(), 1.0F, 1.0F,
						"youarehere.dimension.below_zero", "",
						20, 20, 20),
				new ConfigEnabledCondition(ConfigDefault.Y_LEVEL));
		addYLevelPlace("above_ceiling", new YLevelPlace(127, 256,
						Identifier.withDefaultNamespace("the_nether"),
						SoundEvents.LAVA_POP.location(), 1.0F, 1.0F,
						"youarehere.dimension.below_zero", "",
						20, 20, 20),
				new ConfigEnabledCondition(ConfigDefault.Y_LEVEL));
	}

	private void registerBiomes(HolderLookup.Provider registries) {
		HolderLookup.RegistryLookup<Biome> biomeLookup = registries.lookupOrThrow(Registries.BIOME);
		List<ResourceKey<Biome>> biomes = biomeLookup.listElementIds().toList();
		for (ResourceKey<Biome> reference : biomes) {
			registerBiomePlace(reference);
		}
	}

	private void registerBiomePlace(ResourceKey<Biome> key) {
		Identifier biomeLocation = key.identifier();
		addBiomePlace("biome_" + biomeLocation.getPath(), generateBiomePlace(biomeLocation), new ConfigEnabledCondition(ConfigDefault.BIOME));
	}

	private BiomePlace generateBiomePlace(Identifier biomeLocation) {
		String title = "biome." + biomeLocation.getNamespace() + "." + biomeLocation.getPath();
		return new BiomePlace(biomeLocation, SoundEvents.UI_TOAST_IN.location(), 1.0F, 1.0F, title, "", 20, 20, 20);
	}
}
