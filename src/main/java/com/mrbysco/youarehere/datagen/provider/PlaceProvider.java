package com.mrbysco.youarehere.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.YLevelPlace;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class PlaceProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LogManager.getLogger();
	private final PackOutput output;
	private final String modid;
	private final Map<String, JsonElement> toSerializeBiome = new HashMap<>();
	private final Map<String, JsonElement> toSerializeDimension = new HashMap<>();
	private final Map<String, JsonElement> toSerializeYLevel = new HashMap<>();


	public PlaceProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid) {
		this.output = packOutput;
		this.modid = modid;
	}

	public CompletableFuture<?> run(CachedOutput cache) {
		start();

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		Path biomeFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(YouAreHere.MOD_ID).resolve("biome");
		toSerializeBiome.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) -> {
			Path modifierPath = biomeFolderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
		}));

		Path dimensionFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(YouAreHere.MOD_ID).resolve("dimension");
		toSerializeDimension.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) -> {
			Path modifierPath = dimensionFolderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
		}));

		Path yLevelFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(YouAreHere.MOD_ID).resolve("y_level");
		toSerializeYLevel.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) -> {
			Path modifierPath = yLevelFolderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
		}));

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	protected abstract void start();

	public <T extends BiomePlace> void addBiomePlace(String placeID, T instance, List<ICondition> conditions) {
		JsonElement json = BiomePlace.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeBiome.put(placeID, json);
	}

	public <T extends BiomePlace> void addBiomePlace(String placeID, T instance, ICondition... conditions) {
		addBiomePlace(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends DimensionPlace> void addDimensionPlace(String placeID, T instance, List<ICondition> conditions) {
		JsonElement json = DimensionPlace.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeDimension.put(placeID, json);
	}

	public <T extends DimensionPlace> void addDimensionPlace(String placeID, T instance, ICondition... conditions) {
		addDimensionPlace(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends YLevelPlace> void addYLevelPlace(String placeID, T instance, List<ICondition> conditions) {
		JsonElement json = YLevelPlace.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeYLevel.put(placeID, json);
	}

	public <T extends YLevelPlace> void addYLevelPlace(String placeID, T instance, ICondition... conditions) {
		addYLevelPlace(placeID, instance, Arrays.asList(conditions));
	}

	@Override
	public String getName() {
		return "Places: " + modid;
	}
}
