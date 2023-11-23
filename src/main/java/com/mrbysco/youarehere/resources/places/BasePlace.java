package com.mrbysco.youarehere.resources.places;

import com.mrbysco.youarehere.registry.condition.PlaceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public abstract class BasePlace {
	protected final ResourceLocation soundLocation;
	protected final float volume, pitch;
	protected final String title;
	protected final String subtitle;
	protected final int duration;
	protected final int fadeInDuration;
	protected final int fadeOutDuration;

	public BasePlace(ResourceLocation soundLocation, float volume, float pitch, String title,
					 String subtitle, int duration, int fadeInDuration, int fadeOutDuration) {
		this.soundLocation = soundLocation;
		this.volume = volume;
		this.pitch = pitch;
		this.title = title;
		this.subtitle = subtitle;
		this.duration = duration;
		this.fadeInDuration = fadeInDuration;
		this.fadeOutDuration = fadeOutDuration;
	}

	public ResourceLocation soundLocation() {
		return soundLocation;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
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
		return Objects.equals(this.soundLocation, that.soundLocation) &&
				Objects.equals(this.volume, that.volume) &&
				Objects.equals(this.pitch, that.pitch) &&
				Objects.equals(this.title, that.title) &&
				Objects.equals(this.subtitle, that.subtitle) &&
				this.duration == that.duration &&
				this.fadeInDuration == that.fadeInDuration &&
				this.fadeOutDuration == that.fadeOutDuration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
	}

	@Override
	public String toString() {
		return "BasePlace[" +
				"soundLocation=" + soundLocation + ", " +
				"volume=" + volume + ", " +
				"pitch=" + pitch + ", " +
				"title=" + title + ", " +
				"subtitle=" + subtitle + ", " +
				"duration=" + duration + ", " +
				"fadeInDuration=" + fadeInDuration + ", " +
				"fadeOutDuration=" + fadeOutDuration + ']';
	}

	public abstract boolean matches(Player player);

	public abstract PlaceType getType();
}
