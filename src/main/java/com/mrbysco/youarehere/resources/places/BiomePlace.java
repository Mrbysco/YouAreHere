package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class BiomePlace extends BasePlace {
	private final ResourceLocation biomeLocation;

	public BiomePlace(ResourceLocation id, ResourceLocation soundLocation, float volume, float pitch, String title,
					  String subtitle, int duration, int fadeInDuration, int fadeOutDuration, ResourceLocation biomeLocation) {
		super(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.biomeLocation = biomeLocation;
	}

	public ResourceLocation biomeLocation() {
		return biomeLocation;
	}

	public int hashCode() {
		return Objects.hash(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, biomeLocation);
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
				"biomeLocation=" + biomeLocation + ']';
	}

	@Override
	public boolean matches(Player player) {
		BlockPos pos = player.blockPosition();
		ResourceLocation biomeLocation = player.level.getBiome(pos).unwrapKey().get().location();
		return biomeLocation != null && biomeLocation.equals(this.biomeLocation());
	}

	@Override
	public JsonObject toJson(JsonObject jsonObject) {
		JsonObject jsonobject = super.toJson(jsonObject);

		jsonobject.addProperty("biome", this.biomeLocation == null ? "" : this.biomeLocation.toString());

		return jsonobject;
	}

	@Override
	public PlaceType getType() {
		return PlaceTypeRegistry.BIOME_TYPE.get();
	}

	public static class Serializer implements PlaceType<BiomePlace> {
		public BiomePlace fromJson(ResourceLocation id, JsonObject jsonObject) {
			String title = GsonHelper.getAsString(jsonObject, "title", "");
			String subtitle = GsonHelper.getAsString(jsonObject, "subtitle", "");
			int duration = GsonHelper.getAsInt(jsonObject, "duration", 20);
			int fadeInDuration = GsonHelper.getAsInt(jsonObject, "fadeInDuration", 20);
			int fadeOutDuration = GsonHelper.getAsInt(jsonObject, "fadeOutDuration", 20);

			String sound = GsonHelper.getAsString(jsonObject, "soundLocation", "");
			ResourceLocation soundLocation = sound.isEmpty() ? null : ResourceLocation.tryParse(sound);
			float volume = GsonHelper.getAsFloat(jsonObject, "volume", 1.0F);
			float pitch = GsonHelper.getAsFloat(jsonObject, "pitch", 1.0F);

			if (!jsonObject.has("biome"))
				throw new com.google.gson.JsonSyntaxException("Missing biome, expected to find a location string");
			ResourceLocation biomeLocation = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "biome", ""));

			return new BiomePlace(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, biomeLocation);
		}

		public BiomePlace fromNetwork(ResourceLocation id, FriendlyByteBuf friendlyByteBuf) {
			String title = friendlyByteBuf.readUtf();
			String subtitle = friendlyByteBuf.readUtf();
			int duration = friendlyByteBuf.readVarInt();
			int fadeInDuration = friendlyByteBuf.readVarInt();
			int fadeOutDuration = friendlyByteBuf.readVarInt();

			String sound = friendlyByteBuf.readUtf();
			float volume = friendlyByteBuf.readFloat();
			float pitch = friendlyByteBuf.readFloat();
			String biome = friendlyByteBuf.readUtf();
			ResourceLocation soundLocation = sound.isEmpty() ? null : ResourceLocation.tryParse(sound);
			ResourceLocation biomeLocation = biome.isEmpty() ? null : ResourceLocation.tryParse(biome);

			return new BiomePlace(id, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration, biomeLocation);
		}

		public void toNetwork(FriendlyByteBuf friendlyByteBuf, BiomePlace biomePlace) {
			friendlyByteBuf.writeUtf(biomePlace.title);
			friendlyByteBuf.writeUtf(biomePlace.subtitle);
			friendlyByteBuf.writeVarInt(biomePlace.duration);
			friendlyByteBuf.writeVarInt(biomePlace.fadeInDuration);
			friendlyByteBuf.writeVarInt(biomePlace.fadeOutDuration);

			friendlyByteBuf.writeUtf(biomePlace.soundLocation == null ? "" : biomePlace.soundLocation.toString());
			friendlyByteBuf.writeFloat(biomePlace.volume);
			friendlyByteBuf.writeFloat(biomePlace.pitch);
			friendlyByteBuf.writeUtf(biomePlace.biomeLocation == null ? "" : biomePlace.biomeLocation.toString());
		}
	}
}
