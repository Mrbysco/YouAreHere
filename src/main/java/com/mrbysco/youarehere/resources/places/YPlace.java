package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class YPlace extends BasePlace {
	private final int minY;
	private final int maxY;
	@Nullable
	private final ResourceLocation dimensionLocation;

	public YPlace(ResourceLocation id, ResourceLocation soundLocation, String title, String subtitle, int duration,
				  int fadeInDuration, int fadeOutDuration, int minY, int maxY, ResourceLocation dimensionLocation) {
		super(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.minY = minY;
		this.maxY = maxY;
		this.dimensionLocation = dimensionLocation;
	}

	public int minY() {
		return minY;
	}

	public int maxY() {
		return maxY;
	}

	public ResourceLocation getDimensionLocation() {
		return dimensionLocation;
	}

	public int hashCode() {
		return Objects.hash(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY, dimensionLocation);
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
				"maxY=" + maxY + ", " +
				"dimensionLocation=" + dimensionLocation + ']';
	}

	@Override
	public boolean matches(Player player) {
		boolean dimensionMatches = getDimensionLocation() == null || player.level.dimension().location().equals(getDimensionLocation());
		return player.getY() >= this.minY() && player.getY() <= this.maxY() && dimensionMatches;
	}

	@Override
	public JsonObject toJson(JsonObject jsonObject) {
		JsonObject jsonobject = super.toJson(jsonObject);

		jsonobject.addProperty("minY", minY);
		jsonobject.addProperty("maxY", maxY);
		if (dimensionLocation != null) {
			jsonobject.addProperty("dimension", dimensionLocation.toString());
		}

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

			if (!jsonObject.has("minY"))
				throw new com.google.gson.JsonSyntaxException("Missing minY, expected to find a minY integer");
			int minY = GsonHelper.getAsInt(jsonObject, "minY");
			if (!jsonObject.has("maxY"))
				throw new com.google.gson.JsonSyntaxException("Missing maxY, expected to find a maxY integer");
			int maxY = GsonHelper.getAsInt(jsonObject, "maxY");
			ResourceLocation dimension = jsonObject.has("dimension") ? ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "dimension", "")) : null;

			return new YPlace(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY, dimension);
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
			String dimension = friendlyByteBuf.readUtf();
			ResourceLocation dimensionLocation = dimension.isEmpty() ? null : ResourceLocation.tryParse(dimension);

			return new YPlace(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration, minY, maxY, dimensionLocation);
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
			friendlyByteBuf.writeUtf(biomePlace.dimensionLocation == null ? "" : biomePlace.dimensionLocation.toString());
		}
	}
}
