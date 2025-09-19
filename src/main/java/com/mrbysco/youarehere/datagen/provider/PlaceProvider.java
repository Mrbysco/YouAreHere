package com.mrbysco.youarehere.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.YLevelPlace;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class PlaceProvider implements DataProvider {
	private final PackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final String modid;
	private final Map<String, WithConditions<BiomePlace>> toSerializeBiome = new HashMap<>();
	private final Map<String, WithConditions<DimensionPlace>> toSerializeDimension = new HashMap<>();
	private final Map<String, WithConditions<YLevelPlace>> toSerializeYLevel = new HashMap<>();


	public PlaceProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modid) {
		this.output = packOutput;
		this.registries = registries;
		this.modid = modid;
	}

	@Override
	public final CompletableFuture<?> run(CachedOutput cache) {
		return this.registries.thenCompose(registries -> this.run(cache, registries));
	}

	public CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
		start(registries);

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		Path biomeFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(YouAreHere.MOD_ID).resolve("biome");
		for (var entry : toSerializeBiome.entrySet()) {
			var name = entry.getKey();
			var modifier = entry.getValue();
			Path modifierPath = biomeFolderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, registries, BiomePlace.CONDITIONAL_CODEC, Optional.of(modifier), modifierPath));
		}

		Path dimensionFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(YouAreHere.MOD_ID).resolve("dimension");
		for (var entry : toSerializeDimension.entrySet()) {
			var name = entry.getKey();
			var modifier = entry.getValue();
			Path modifierPath = dimensionFolderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, registries, DimensionPlace.CONDITIONAL_CODEC, Optional.of(modifier), modifierPath));
		}

		Path yLevelFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(YouAreHere.MOD_ID).resolve("y_level");
		for (var entry : toSerializeYLevel.entrySet()) {
			var name = entry.getKey();
			var modifier = entry.getValue();
			Path modifierPath = yLevelFolderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, registries, YLevelPlace.CONDITIONAL_CODEC, Optional.of(modifier), modifierPath));
		}

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	protected abstract void start(HolderLookup.Provider registries);

	public <T extends BiomePlace> void addBiomePlace(String placeID, T instance, List<ICondition> conditions) {
		this.toSerializeBiome.put(placeID, new WithConditions<>(conditions, instance));
	}

	public <T extends BiomePlace> void addBiomePlace(String placeID, T instance, ICondition... conditions) {
		addBiomePlace(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends DimensionPlace> void addDimensionPlace(String placeID, T instance, List<ICondition> conditions) {
		this.toSerializeDimension.put(placeID, new WithConditions<>(conditions, instance));
	}

	public <T extends DimensionPlace> void addDimensionPlace(String placeID, T instance, ICondition... conditions) {
		addDimensionPlace(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends YLevelPlace> void addYLevelPlace(String placeID, T instance, List<ICondition> conditions) {
		this.toSerializeYLevel.put(placeID, new WithConditions<>(conditions, instance));
	}

	public <T extends YLevelPlace> void addYLevelPlace(String placeID, T instance, ICondition... conditions) {
		addYLevelPlace(placeID, instance, Arrays.asList(conditions));
	}

	@Override
	public String getName() {
		return "Places: " + modid;
	}
}
