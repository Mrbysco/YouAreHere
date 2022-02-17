package com.mrbysco.youarehere.datagen.provider.builder;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import com.mrbysco.youarehere.resources.places.PlaceType;
import com.mrbysco.youarehere.resources.places.YPlace;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class YPlaceBuilder implements PlaceBuilder {
	private ResourceLocation soundLocation;
	private String title;
	private String subtitle;
	private int duration;
	private int fadeInDuration;
	private int fadeOutDuration;
	private int minY;
	private int maxY;

	public YPlaceBuilder(int minY, int maxY) {
		this.minY = minY;
		this.maxY = maxY;
	}

	public static YPlaceBuilder y(int minY, int maxY) {
		return new YPlaceBuilder(minY, maxY);
	}

	public YPlaceBuilder setSoundLocation(ResourceLocation soundLocation) {
		this.soundLocation = soundLocation;
		return this;
	}

	public YPlaceBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public YPlaceBuilder setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public YPlaceBuilder setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public YPlaceBuilder setFadeInDuration(int fadeInDuration) {
		this.fadeInDuration = fadeInDuration;
		return this;
	}

	public YPlaceBuilder setFadeOutDuration(int fadeOutDuration) {
		this.fadeOutDuration = fadeOutDuration;
		return this;
	}

	public YPlaceBuilder setMinY(int minY) {
		this.minY = minY;
		return this;
	}

	public YPlaceBuilder setMaxY(int maxY) {
		this.maxY = maxY;
		return this;
	}

	public YPlace build(ResourceLocation id) {
		return new YPlace(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY);
	}

	@Override
	public ResourceLocation getDefaultID() {
		return new ResourceLocation(YouAreHere.MOD_ID, minY + "_to_" + maxY);
	}

	@Override
	public void save(Consumer<FinishedPlace> placeConsumer, ResourceLocation location) {
		placeConsumer.accept(new YPlaceBuilder.Result(location, build(location)));
	}

	public static class Result implements FinishedPlace {
		private final ResourceLocation id;
		protected final YPlace yPlace;

		public Result(ResourceLocation id, YPlace yPlace) {
			this.id = id;
			this.yPlace = yPlace;
		}

		public void serializePlaceData(JsonObject jsonobject) {
			yPlace.toJson(jsonobject);
		}

		public PlaceType<?> getType() {
			return PlaceTypeRegistry.Y_LEVEL_TYPE.get();
		}

		public ResourceLocation getId() {
			return this.id;
		}
	}
}