package com.mrbysco.youarehere.datagen.provider.builder;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class DimensionPlaceBuilder implements PlaceBuilder {
	private ResourceLocation soundLocation;
	private String title;
	private String subtitle;
	private int duration;
	private int fadeInDuration;
	private int fadeOutDuration;
	private ResourceLocation dimensionLocation;

	public DimensionPlaceBuilder(ResourceLocation dimensionLocation) {
		this.dimensionLocation = dimensionLocation;
	}

	public static DimensionPlaceBuilder dimension(ResourceLocation dimensionLocation) {
		return new DimensionPlaceBuilder(dimensionLocation);
	}

	public DimensionPlaceBuilder setSoundLocation(ResourceLocation soundLocation) {
		this.soundLocation = soundLocation;
		return this;
	}

	public DimensionPlaceBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public DimensionPlaceBuilder setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public DimensionPlaceBuilder setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public DimensionPlaceBuilder setFadeInDuration(int fadeInDuration) {
		this.fadeInDuration = fadeInDuration;
		return this;
	}

	public DimensionPlaceBuilder setFadeOutDuration(int fadeOutDuration) {
		this.fadeOutDuration = fadeOutDuration;
		return this;
	}

	public DimensionPlaceBuilder setDimensionLocation(ResourceLocation dimensionLocation) {
		this.dimensionLocation = dimensionLocation;
		return this;
	}

	public DimensionPlace build(ResourceLocation id) {
		return new DimensionPlace(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, dimensionLocation);
	}

	@Override
	public ResourceLocation getDefaultID() {
		return new ResourceLocation(YouAreHere.MOD_ID, this.dimensionLocation.getNamespace());
	}

	@Override
	public void save(Consumer<FinishedPlace> placeConsumer, ResourceLocation location) {
		placeConsumer.accept(new Result(location, build(location)));
	}

	public static class Result implements FinishedPlace {
		private final ResourceLocation id;
		protected final DimensionPlace dimensionPlace;

		public Result(ResourceLocation id, DimensionPlace dimensionPlace) {
			this.id = id;
			this.dimensionPlace = dimensionPlace;
		}

		public void serializePlaceData(JsonObject jsonobject) {
			dimensionPlace.toJson(jsonobject);
		}

		public PlaceType<?> getType() {
			return PlaceTypeRegistry.DIMENSION_TYPE.get();
		}

		public ResourceLocation getId() {
			return this.id;
		}
	}
}