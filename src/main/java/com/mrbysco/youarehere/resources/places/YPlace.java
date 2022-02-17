package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;

public class YPlace extends BasePlace{
	private final int minY;
	private final int maxY;

	public YPlace(ResourceLocation id, ResourceLocation soundLocation, String title, String subtitle, int duration,
				  int fadeInDuration, int fadeOutDuration, int minY, int maxY) {
		super(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.minY = minY;
		this.maxY = maxY;
	}

	public int minY() {
		return minY;
	}

	public int maxY() {
		return maxY;
	}

	public int hashCode() {
		return Objects.hash(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY);
	}

	@Override
	public String toString() {
		return "BasePlace[" +
				"id=" + id + ", " +
				"soundLocation=" + soundLocation + ", " +
				"title=" + title + ", " +
				"subtitle=" + subtitle + ", " +
				"duration=" + duration + ", " +
				"fadeInDuration=" + fadeInDuration + ", " +
				"fadeOutDuration=" + fadeOutDuration + ", " +
				"minY=" + minY + ", " +
				"maxY=" + maxY + ']';
	}

	@Override
	public boolean matches(Player player) {
		return player.getY() >= this.minY() && player.getY() <= this.maxY();
	}

	@Override
	public JsonObject toJson(JsonObject jsonObject) {
		JsonObject jsonobject = super.toJson(jsonObject);

		jsonobject.addProperty("minY", minY);
		jsonobject.addProperty("maxY", maxY);

		return jsonobject;
	}

	@Override
	public PlaceType<?> getType() {
		return PlaceTypeRegistry.Y_LEVEL_TYPE.get();
	}

	public static class Serializer extends ForgeRegistryEntry<PlaceType<?>> implements PlaceType<YPlace> {
		public YPlace fromJson(ResourceLocation id, JsonObject jsonObject) {
			String title = GsonHelper.getAsString(jsonObject, "title", "");
			String subtitle = GsonHelper.getAsString(jsonObject, "subtitle", "");
			int duration = GsonHelper.getAsInt(jsonObject, "duration", 20);
			int fadeInDuration = GsonHelper.getAsInt(jsonObject, "fadeInDuration", 20);
			int fadeOutDuration = GsonHelper.getAsInt(jsonObject, "fadeOutDuration", 20);

			String sound = GsonHelper.getAsString(jsonObject, "soundLocation", "");
			ResourceLocation soundLocation = sound.isEmpty() ? null : ResourceLocation.tryParse(sound);

			if (!jsonObject.has("minY")) throw new com.google.gson.JsonSyntaxException("Missing minY, expected to find a minY integer");
			int minY = GsonHelper.getAsInt(jsonObject, "minY");
			if (!jsonObject.has("maxY")) throw new com.google.gson.JsonSyntaxException("Missing maxY, expected to find a maxY integer");
			int maxY = GsonHelper.getAsInt(jsonObject, "maxY");

			return new YPlace(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY);
		}

		public YPlace fromNetwork(ResourceLocation id, FriendlyByteBuf friendlyByteBuf) {
			String title = friendlyByteBuf.readUtf();
			String subtitle = friendlyByteBuf.readUtf();
			int duration = friendlyByteBuf.readVarInt();
			int fadeInDuration = friendlyByteBuf.readVarInt();
			int fadeOutDuration = friendlyByteBuf.readVarInt();
			String sound = friendlyByteBuf.readUtf();
			ResourceLocation soundLocation = sound.isEmpty() ? null : ResourceLocation.tryParse(sound);
			int minY = friendlyByteBuf.readVarInt();
			int maxY = friendlyByteBuf.readVarInt();

			return new YPlace(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY);
		}

		public void toNetwork(FriendlyByteBuf friendlyByteBuf, YPlace biomePlace) {
			friendlyByteBuf.writeUtf(biomePlace.title);
			friendlyByteBuf.writeUtf(biomePlace.subtitle);
			friendlyByteBuf.writeVarInt(biomePlace.duration);
			friendlyByteBuf.writeVarInt(biomePlace.fadeInDuration);
			friendlyByteBuf.writeVarInt(biomePlace.fadeOutDuration);

			friendlyByteBuf.writeUtf(biomePlace.soundLocation == null ? "" : biomePlace.soundLocation.toString());
			friendlyByteBuf.writeVarInt(biomePlace.minY);
			friendlyByteBuf.writeVarInt(biomePlace.maxY);
		}
	}
}
