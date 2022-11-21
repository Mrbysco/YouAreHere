package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;

public class DimensionPlace extends BasePlace {
	private final ResourceLocation dimensionLocation;

	public DimensionPlace(ResourceLocation id, ResourceLocation soundLocation, float volume, float pitch, String title,
						  String subtitle, int duration, int fadeInDuration, int fadeOutDuration, ResourceLocation dimensionLocation) {
		super(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.dimensionLocation = dimensionLocation;
	}

	public ResourceLocation dimensionLocation() {
		return dimensionLocation;
	}

	public int hashCode() {
		return Objects.hash(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, dimensionLocation);
	}

	@Override
	public String toString() {
		return "BasePlace[" +
				"id=" + id + ", " +
				"soundLocation=" + soundLocation + ", " +
				"volume=" + volume + ", " +
				"pitch=" + pitch + ", " +
				"title=" + title + ", " +
				"subtitle=" + subtitle + ", " +
				"duration=" + duration + ", " +
				"fadeInDuration=" + fadeInDuration + ", " +
				"fadeOutDuration=" + fadeOutDuration + ", " +
				"dimensionLocation=" + dimensionLocation + ']';
	}

	@Override
	public boolean matches(Player player) {
		return player.level.dimension().location().equals(this.dimensionLocation());
	}

	@Override
	public JsonObject toJson(JsonObject jsonObject) {
		JsonObject jsonobject = super.toJson(jsonObject);

		jsonobject.addProperty("dimension", this.dimensionLocation == null ? "" : this.dimensionLocation.toString());

		return jsonobject;
	}

	@Override
	public PlaceType getType() {
		return PlaceTypeRegistry.DIMENSION_TYPE.get();
	}

	public static class Serializer extends ForgeRegistryEntry<PlaceType<?>> implements PlaceType<DimensionPlace> {
		public DimensionPlace fromJson(ResourceLocation id, JsonObject jsonObject) {
			String title = GsonHelper.getAsString(jsonObject, "title", "");
			String subtitle = GsonHelper.getAsString(jsonObject, "subtitle", "");
			int duration = GsonHelper.getAsInt(jsonObject, "duration", 20);
			int fadeInDuration = GsonHelper.getAsInt(jsonObject, "fadeInDuration", 20);
			int fadeOutDuration = GsonHelper.getAsInt(jsonObject, "fadeOutDuration", 20);

			String sound = GsonHelper.getAsString(jsonObject, "soundLocation", "");
			ResourceLocation soundLocation = sound.isEmpty() ? null : ResourceLocation.tryParse(sound);
			float volume = GsonHelper.getAsFloat(jsonObject, "volume", 1.0F);
			float pitch = GsonHelper.getAsFloat(jsonObject, "pitch", 1.0F);

			if (!jsonObject.has("dimension"))
				throw new com.google.gson.JsonSyntaxException("Missing dimension, expected to find a location string");
			ResourceLocation dimensionLocation = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "dimension", ""));

			return new DimensionPlace(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, dimensionLocation);
		}

		public DimensionPlace fromNetwork(ResourceLocation id, FriendlyByteBuf friendlyByteBuf) {
			String title = friendlyByteBuf.readUtf();
			String subtitle = friendlyByteBuf.readUtf();
			int duration = friendlyByteBuf.readVarInt();
			int fadeInDuration = friendlyByteBuf.readVarInt();
			int fadeOutDuration = friendlyByteBuf.readVarInt();

			String sound = friendlyByteBuf.readUtf();
			float volume = friendlyByteBuf.readFloat();
			float pitch = friendlyByteBuf.readFloat();
			String dimension = friendlyByteBuf.readUtf();
			ResourceLocation soundLocation = sound.isEmpty() ? null : ResourceLocation.tryParse(sound);
			ResourceLocation dimensionLocation = dimension.isEmpty() ? null : ResourceLocation.tryParse(dimension);

			return new DimensionPlace(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, dimensionLocation);
		}

		public void toNetwork(FriendlyByteBuf friendlyByteBuf, DimensionPlace dimensionPlace) {
			friendlyByteBuf.writeUtf(dimensionPlace.title);
			friendlyByteBuf.writeUtf(dimensionPlace.subtitle);
			friendlyByteBuf.writeVarInt(dimensionPlace.duration);
			friendlyByteBuf.writeVarInt(dimensionPlace.fadeInDuration);
			friendlyByteBuf.writeVarInt(dimensionPlace.fadeOutDuration);

			friendlyByteBuf.writeUtf(dimensionPlace.soundLocation == null ? "" : dimensionPlace.soundLocation.toString());
			friendlyByteBuf.writeFloat(dimensionPlace.volume);
			friendlyByteBuf.writeFloat(dimensionPlace.pitch);
			friendlyByteBuf.writeUtf(dimensionPlace.dimensionLocation == null ? "" : dimensionPlace.dimensionLocation.toString());
		}
	}
}
