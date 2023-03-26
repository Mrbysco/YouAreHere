package com.mrbysco.youarehere.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.BiomePlaceBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.YPlaceBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlaceProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LogManager.getLogger();
	protected final CompletableFuture<HolderLookup.Provider> lookupProvider;

	protected final PackOutput.PathProvider placesPathProvider;
	private final String modID;

	public PlaceProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modID) {
		this.placesPathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "places");
		this.lookupProvider = lookupProvider;
		this.modID = modID;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return this.lookupProvider.thenCompose((p_255494_) -> {
			List<CompletableFuture<?>> list = new ArrayList<>();
			Set<ResourceLocation> set = Sets.newHashSet();
			this.createPlaces((finishedPlace) -> {
				if (!set.add(finishedPlace.getId())) {
					throw new IllegalStateException("Duplicate Places " + finishedPlace.getId());
				} else {
					list.add(DataProvider.saveStable(cache, finishedPlace.serializePlace(), this.placesPathProvider.json(finishedPlace.getId())));
				}
			});

			return CompletableFuture.allOf(list.toArray((p_253414_) -> {
				return new CompletableFuture[p_253414_];
			}));
		});
	}

	public void createPlaces(Consumer<FinishedPlace> consumer) {

	}

	protected void addBiomePlace(Consumer<FinishedPlace> consumer, String name, BiomePlaceBuilder builder) {
		builder.save(consumer, new ResourceLocation(modID, name));
	}

	protected void addYPlace(Consumer<FinishedPlace> consumer, String name, YPlaceBuilder builder) {
		builder.save(consumer, new ResourceLocation(modID, name));
	}

	@Override
	public String getName() {
		return "Places: " + modID;
	}
}
