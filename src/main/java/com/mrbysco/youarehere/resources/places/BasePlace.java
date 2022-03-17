package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public abstract class BasePlace {
	protected final ResourceLocation id;
	protected final ResourceLocation soundLocation;
	protected final String title;
	protected final String subtitle;
	protected final int duration;
	protected final int fadeInDuration;
	protected final int fadeOutDuration;

	public BasePlace(ResourceLocation id, ResourceLocation soundLocation, String title, String subtitle,
					 int duration, int fadeInDuration, int fadeOutDuration) {
		this.id = id;
		this.soundLocation = soundLocation;
		this.title = title;
		this.subtitle = subtitle;
		this.duration = duration;
		this.fadeInDuration = fadeInDuration;
		this.fadeOutDuration = fadeOutDuration;
	}

	public ResourceLocation id() {
		return id;
	}

	public ResourceLocation soundLocation() {
		return soundLocation;
	}

	public String title() {
		return title;
	}

	public String subtitle() {
		return subtitle;
	}

	public int duration() {
		return duration;
	}

	public int fadeInDuration() {
		return fadeInDuration;
	}

	public int fadeOutDuration() {
		return fadeOutDuration;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BasePlace) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.soundLocation, that.soundLocation) &&
				Objects.equals(this.title, that.title) &&
				Objects.equals(this.subtitle, that.subtitle) &&
				this.duration == that.duration &&
				this.fadeInDuration == that.fadeInDuration &&
				this.fadeOutDuration == that.fadeOutDuration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, soundLocation, title, subtitle, duration, fadeInDuration, fadeOutDuration);
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
				"fadeOutDuration=" + fadeOutDuration + ']';
	}

	public JsonObject toJson(JsonObject jsonobject) {
		if (this.soundLocation != null) {
			jsonobject.addProperty("soundLocation", this.soundLocation.toString());
		}
		jsonobject.addProperty("title", this.title);
		jsonobject.addProperty("subtitle", this.subtitle);
		if (this.duration > 0)
			jsonobject.addProperty("duration", this.duration);
		if (this.fadeInDuration > 0)
			jsonobject.addProperty("fadeInDuration", fadeInDuration);
		if (this.fadeOutDuration > 0)
			jsonobject.addProperty("fadeOutDuration", fadeOutDuration);

		return jsonobject;
	}

	public abstract boolean matches(Player player);

	public abstract PlaceType getType();
}
