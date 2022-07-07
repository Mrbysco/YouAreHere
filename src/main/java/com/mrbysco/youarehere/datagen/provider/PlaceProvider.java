package com.mrbysco.youarehere.datagen.provider;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mrbysco.youarehere.datagen.provider.builder.BiomePlaceBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.YPlaceBuilder;
import com.mrbysco.youarehere.resources.PlaceManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class PlaceProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LogManager.getLogger();

	private final DataGenerator generator;
	private final String modID;

	public PlaceProvider(DataGenerator generator, String modID) {
		this.generator = generator;
		this.modID = modID;
	}

	@Override
	public void run(CachedOutput cache) throws IOException {
		Path outputFolder = this.generator.getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		this.createPlaces((finishedPlace) -> {
			if (!set.add(finishedPlace.getId())) {
				throw new IllegalStateException("Duplicate Block Properties " + finishedPlace.getId());
			} else {
				savePlaces(cache, finishedPlace.serializePlace(),
						outputFolder.resolve("data/" + finishedPlace.getId().getNamespace() + "/" + PlaceManager.FOLDER_NAME + "/" + finishedPlace.getId().getPath() + ".json"));
			}
		});
	}

	public void createPlaces(Consumer<FinishedPlace> consumer) {

	}

	private void addBiomePlace(Consumer<FinishedPlace> consumer, String name, BiomePlaceBuilder builder) {
		builder.save(consumer, new ResourceLocation(modID, name));
	}

	private void addYPlace(Consumer<FinishedPlace> consumer, String name, YPlaceBuilder builder) {
		builder.save(consumer, new ResourceLocation(modID, name));
	}

	private static void savePlaces(CachedOutput cache, JsonObject jsonObject, Path path) {
		try {
			DataProvider.saveStable(cache, jsonObject, path);
		} catch (IOException ioexception) {
			LOGGER.error("Couldn't save Place Info {}", path, ioexception);
		}
	}

	@Override
	public String getName() {
		return "Places: " + modID;
	}
}
