package com.mrbysco.youarehere.datagen.provider.builder;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class BiomePlaceBuilder implements PlaceBuilder {
	private ResourceLocation soundLocation;
	private float volume, pitch;
	private String title;
	private String subtitle;
	private int duration;
	private int fadeInDuration;
	private int fadeOutDuration;
	private ResourceLocation biomeLocation;

	public BiomePlaceBuilder(ResourceLocation biomeLocation) {
		this.biomeLocation = biomeLocation;
		this.volume = 1.0F;
		this.pitch = 1.0F;
	}

	public static BiomePlaceBuilder biome(ResourceLocation biomeLocation) {
		return new BiomePlaceBuilder(biomeLocation);
	}

	public BiomePlaceBuilder setSoundLocation(ResourceLocation soundLocation) {
		this.soundLocation = soundLocation;
		return this;
	}

	public BiomePlaceBuilder setVolume(float volume) {
		this.volume = volume;
		return this;
	}

	public BiomePlaceBuilder setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	public BiomePlaceBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public BiomePlaceBuilder setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public BiomePlaceBuilder setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public BiomePlaceBuilder setFadeInDuration(int fadeInDuration) {
		this.fadeInDuration = fadeInDuration;
		return this;
	}

	public BiomePlaceBuilder setFadeOutDuration(int fadeOutDuration) {
		this.fadeOutDuration = fadeOutDuration;
		return this;
	}

	public BiomePlaceBuilder setBiomeLocation(ResourceLocation biomeLocation) {
		this.biomeLocation = biomeLocation;
		return this;
	}

	public BiomePlace build(ResourceLocation id) {
		return new BiomePlace(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, biomeLocation);
	}

	@Override
	public ResourceLocation getDefaultID() {
		return new ResourceLocation(YouAreHere.MOD_ID, this.biomeLocation.getNamespace());
	}

	@Override
	public void save(Consumer<FinishedPlace> placeConsumer, ResourceLocation location) {
		placeConsumer.accept(new Result(location, build(location)));
	}

	public static class Result implements FinishedPlace {
		private final ResourceLocation id;
		protected final BiomePlace biomePlace;

		public Result(ResourceLocation id, BiomePlace biomePlace) {
			this.id = id;
			this.biomePlace = biomePlace;
		}

		public void serializePlaceData(JsonObject jsonobject) {
			biomePlace.toJson(jsonobject);
		}

		public PlaceType<?> getType() {
			return PlaceTypeRegistry.BIOME_TYPE.get();
		}

		public ResourceLocation getId() {
			return this.id;
		}
	}
}