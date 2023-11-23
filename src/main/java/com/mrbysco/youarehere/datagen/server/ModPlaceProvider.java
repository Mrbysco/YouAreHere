package com.mrbysco.youarehere.datagen.server;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.PlaceProvider;
import com.mrbysco.youarehere.registry.condition.EnableBiomePlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableDimensionPlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableYLevelPlaces;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.YLevelPlace;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class ModPlaceProvider extends PlaceProvider {
	public ModPlaceProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider, YouAreHere.MOD_ID);
	}

	@Override
	protected void start() {
		registerBiomes();

		addDimensionPlace("dimension_the_nether", new DimensionPlace(new ResourceLocation("the_nether"), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation(), 1.0F, 1.0F, "youarehere.dimension.the_nether", "", 20, 20, 20), EnableDimensionPlacesCondition.INSTANCE);
		addDimensionPlace("dimension_the_end", new DimensionPlace(new ResourceLocation("the_end"), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation(), 1.0F, 1.0F, "youarehere.dimension.the_end", "", 20, 20, 20), EnableDimensionPlacesCondition.INSTANCE);

		addYLevelPlace("y_below_zero", new YLevelPlace(-64, 0, new ResourceLocation("overworld"), SoundEvents.LAVA_POP.getLocation(), 1.0F, 1.0F, "youarehere.dimension.below_zero", "", 20, 20, 20), EnableYLevelPlaces.INSTANCE);
		addYLevelPlace("above_ceiling", new YLevelPlace(127, 256, new ResourceLocation("the_nether"), SoundEvents.LAVA_POP.getLocation(), 1.0F, 1.0F, "youarehere.dimension.below_zero", "", 20, 20, 20), EnableYLevelPlaces.INSTANCE);
	}

	private void registerBiomes() {
		registerBiomePlace(Biomes.PLAINS);
		registerBiomePlace(Biomes.SUNFLOWER_PLAINS);
		registerBiomePlace(Biomes.SNOWY_PLAINS);
		registerBiomePlace(Biomes.ICE_SPIKES);
		registerBiomePlace(Biomes.DESERT);
		registerBiomePlace(Biomes.SWAMP);
		registerBiomePlace(Biomes.MANGROVE_SWAMP);
		registerBiomePlace(Biomes.FOREST);
		registerBiomePlace(Biomes.FLOWER_FOREST);
		registerBiomePlace(Biomes.BIRCH_FOREST);
		registerBiomePlace(Biomes.DARK_FOREST);
		registerBiomePlace(Biomes.OLD_GROWTH_BIRCH_FOREST);
		registerBiomePlace(Biomes.OLD_GROWTH_PINE_TAIGA);
		registerBiomePlace(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
		registerBiomePlace(Biomes.TAIGA);
		registerBiomePlace(Biomes.SNOWY_TAIGA);
		registerBiomePlace(Biomes.SAVANNA);
		registerBiomePlace(Biomes.SAVANNA_PLATEAU);
		registerBiomePlace(Biomes.WINDSWEPT_HILLS);
		registerBiomePlace(Biomes.WINDSWEPT_GRAVELLY_HILLS);
		registerBiomePlace(Biomes.WINDSWEPT_FOREST);
		registerBiomePlace(Biomes.WINDSWEPT_SAVANNA);
		registerBiomePlace(Biomes.JUNGLE);
		registerBiomePlace(Biomes.SPARSE_JUNGLE);
		registerBiomePlace(Biomes.BAMBOO_JUNGLE);
		registerBiomePlace(Biomes.BADLANDS);
		registerBiomePlace(Biomes.ERODED_BADLANDS);
		registerBiomePlace(Biomes.WOODED_BADLANDS);
		registerBiomePlace(Biomes.MEADOW);
		registerBiomePlace(Biomes.CHERRY_GROVE);
		registerBiomePlace(Biomes.GROVE);
		registerBiomePlace(Biomes.SNOWY_SLOPES);
		registerBiomePlace(Biomes.FROZEN_PEAKS);
		registerBiomePlace(Biomes.JAGGED_PEAKS);
		registerBiomePlace(Biomes.STONY_PEAKS);
		registerBiomePlace(Biomes.RIVER);
		registerBiomePlace(Biomes.FROZEN_RIVER);
		registerBiomePlace(Biomes.BEACH);
		registerBiomePlace(Biomes.SNOWY_BEACH);
		registerBiomePlace(Biomes.STONY_SHORE);
		registerBiomePlace(Biomes.WARM_OCEAN);
		registerBiomePlace(Biomes.LUKEWARM_OCEAN);
		registerBiomePlace(Biomes.DEEP_LUKEWARM_OCEAN);
		registerBiomePlace(Biomes.OCEAN);
		registerBiomePlace(Biomes.DEEP_OCEAN);
		registerBiomePlace(Biomes.COLD_OCEAN);
		registerBiomePlace(Biomes.DEEP_COLD_OCEAN);
		registerBiomePlace(Biomes.FROZEN_OCEAN);
		registerBiomePlace(Biomes.DEEP_FROZEN_OCEAN);
		registerBiomePlace(Biomes.MUSHROOM_FIELDS);
		registerBiomePlace(Biomes.DRIPSTONE_CAVES);
		registerBiomePlace(Biomes.LUSH_CAVES);
		registerBiomePlace(Biomes.DEEP_DARK);
		registerBiomePlace(Biomes.NETHER_WASTES);
		registerBiomePlace(Biomes.WARPED_FOREST);
		registerBiomePlace(Biomes.CRIMSON_FOREST);
		registerBiomePlace(Biomes.SOUL_SAND_VALLEY);
		registerBiomePlace(Biomes.BASALT_DELTAS);
		registerBiomePlace(Biomes.THE_END);
		registerBiomePlace(Biomes.END_HIGHLANDS);
		registerBiomePlace(Biomes.END_MIDLANDS);
		registerBiomePlace(Biomes.SMALL_END_ISLANDS);
		registerBiomePlace(Biomes.END_BARRENS);
	}

	private void registerBiomePlace(ResourceKey<Biome> key) {
		ResourceLocation biomeLocation = key.location();
		addBiomePlace("biome_" + biomeLocation.getPath(), generateBiomePlace(biomeLocation), EnableBiomePlacesCondition.INSTANCE);
	}

	private BiomePlace generateBiomePlace(ResourceLocation biomeLocation) {
		String title = "biome." + biomeLocation.getNamespace() + "." + biomeLocation.getPath();
		return new BiomePlace(biomeLocation, SoundEvents.UI_TOAST_IN.getLocation(), 1.0F, 1.0F, title, "", 20, 20, 20);
	}
}
