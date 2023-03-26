package com.mrbysco.youarehere.datagen;

import com.google.common.collect.Sets;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import com.mrbysco.youarehere.datagen.provider.PlaceProvider;
import com.mrbysco.youarehere.datagen.provider.builder.BiomePlaceBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.ConditionalPlace;
import com.mrbysco.youarehere.datagen.provider.builder.DimensionPlaceBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.YPlaceBuilder;
import com.mrbysco.youarehere.registry.PlaceSounds;
import com.mrbysco.youarehere.registry.condition.EnableBiomePlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableDimensionPlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableYPlacesCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistriesHooks;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlaceDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider().thenApply(PlaceDatagen::createLookup);

		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new Places(packOutput, lookupProvider));
			generator.addProvider(event.includeClient(), new PlaceSoundProvider(packOutput, helper));
		}
	}

	private static HolderLookup.Provider createLookup(final HolderLookup.Provider vanillaLookupProvider) {
		final var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

		final RegistrySetBuilder builder = new RegistrySetBuilder();
		builder.add(Registries.BIOME, context -> {
		});

		@SuppressWarnings("UnstableApiUsage") final var allKeys = DataPackRegistriesHooks.getDataPackRegistries()
				.stream()
				.map(RegistryDataLoader.RegistryData::key)
				.collect(Collectors.toSet());

		final var modKeys = Set.copyOf(builder.getEntryKeys());

		final var missingKeys = Sets.difference(allKeys, modKeys);

		missingKeys.forEach(key -> builder.add(
				ResourceKey.create(ResourceKey.createRegistryKey(key.registry()), key.location()),
				context -> {
				}
		));

		return builder.buildPatch(registryAccess, vanillaLookupProvider);
	}

	private static class Places extends PlaceProvider implements IConditionBuilder {
		public Places(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(packOutput, lookupProvider, YouAreHere.MOD_ID);
		}

		@Override
		public void createPlaces(Consumer<FinishedPlace> consumer) {
			//TODO: Figure out how to get all biomes since you can't iterate through the registry anymore
			//This list is all values found in the Biomes class
			List<String> biomeList = List.of("the_void", "plains", "sunflower_plains", "snowy_plains",
					"ice_spikes", "desert", "swamp", "mangrove_swamp", "forest", "flower_forest", "birch_forest",
					"dark_forest", "old_growth_birch_forest", "old_growth_pine_taiga", "old_growth_spruce_taiga",
					"taiga", "snowy_taiga", "savanna", "savanna_plateau", "windswept_hills", "windswept_gravelly_hills",
					"windswept_forest", "windswept_savanna", "jungle", "sparse_jungle", "bamboo_jungle", "badlands",
					"eroded_badlands", "wooded_badlands", "meadow", "grove", "snowy_slopes", "frozen_peaks", "jagged_peaks",
					"stony_peaks", "river", "frozen_river", "beach", "snowy_beach", "stony_shore", "warm_ocean",
					"lukewarm_ocean", "deep_lukewarm_ocean", "ocean", "deep_ocean", "cold_ocean", "deep_cold_ocean",
					"frozen_ocean", "deep_frozen_ocean", "mushroom_fields", "dripstone_caves", "lush_caves", "deep_dark",
					"nether_wastes", "warped_forest", "crimson_forest", "soul_sand_valley", "basalt_deltas", "the_end",
					"end_highlands", "end_midlands", "small_end_islands", "end_barrens");
			for (String biomeName : biomeList) {
				ResourceLocation biomeLocation = new ResourceLocation(biomeName);
				ResourceLocation id = new ResourceLocation(YouAreHere.MOD_ID, "biome_" + biomeLocation.getPath());
				new ConditionalPlace.Builder()
						.addCondition(
								new EnableBiomePlacesCondition()
						)
						.addPlace(
								BiomePlaceBuilder.biome(biomeLocation)
										.setTitle("biome." + biomeLocation.getNamespace() + "." + biomeLocation.getPath())
										.setSoundLocation(SoundEvents.UI_TOAST_IN.getLocation())
										::save
						)
						.build(consumer, id);
			}

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableDimensionPlacesCondition()
					)
					.addPlace(
							DimensionPlaceBuilder.dimension(new ResourceLocation("the_nether"))
									.setTitle("youarehere.dimension.the_nether")
									.setSoundLocation(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation())
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "dimension_the_nether"));

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableDimensionPlacesCondition()
					)
					.addPlace(
							DimensionPlaceBuilder.dimension(new ResourceLocation("the_end"))
									.setTitle("youarehere.dimension.the_end")
									.setSoundLocation(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation())
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "dimension_the_end"));

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableYPlacesCondition()
					)
					.addPlace(
							YPlaceBuilder.y(-64, 0)
									.setTitle("Below Zero")
									.setSoundLocation(SoundEvents.LAVA_POP.getLocation())
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "y_below_zero"));

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableYPlacesCondition()
					)
					.addPlace(
							YPlaceBuilder.y(127, 256)
									.setTitle("On the ceiling")
									.setSoundLocation(SoundEvents.LAVA_POP.getLocation())
									.setDimensionLocation(new ResourceLocation("the_nether"))
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "above_ceiling"));
		}
	}

	public static class PlaceSoundProvider extends SoundDefinitionsProvider {
		public PlaceSoundProvider(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, YouAreHere.MOD_ID, helper);
		}

		@Override
		public void registerSounds() {
			this.add(PlaceSounds.SOUND_1, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_2, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_3, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_4, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_5, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_6, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_7, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_8, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_9, definition()
					.with(sound(modLoc("empty")))
			);
			this.add(PlaceSounds.SOUND_10, definition()
					.with(sound(modLoc("empty")))
			);
		}

		public ResourceLocation modLoc(String name) {
			return new ResourceLocation(YouAreHere.MOD_ID, name);
		}
	}
}
