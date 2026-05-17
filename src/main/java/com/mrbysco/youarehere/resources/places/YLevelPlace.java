package com.mrbysco.youarehere.resources.places;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.condition.PlaceType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class YLevelPlace extends BasePlace {
	public static final ResourceKey<Registry<YLevelPlace>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			YouAreHere.modLoc("y_level"));
	public static final Codec<YLevelPlace> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
			RecordCodecBuilder.create(
					apply -> apply.group(
									Codec.INT.fieldOf("minY").forGetter(YLevelPlace::minY),
									Codec.INT.fieldOf("maxY").forGetter(YLevelPlace::maxY),
									Identifier.CODEC.optionalFieldOf("dimension").forGetter(YLevelPlace::dimensionLocation),
									Identifier.CODEC.fieldOf("sound").forGetter(BasePlace::soundLocation),
									Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(BasePlace::getVolume),
									Codec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(BasePlace::getPitch),
									Codec.STRING.fieldOf("title").forGetter(BasePlace::title),
									Codec.STRING.optionalFieldOf("subtitle", "").forGetter(BasePlace::subtitle),
									Codec.INT.optionalFieldOf("duration", 20).forGetter(BasePlace::duration),
									Codec.INT.optionalFieldOf("fadeInDuration", 20).forGetter(BasePlace::fadeInDuration),
									Codec.INT.optionalFieldOf("fadeOutDuration", 20).forGetter(BasePlace::fadeOutDuration)
							)
							.apply(apply, YLevelPlace::new)
			)
	);
	public static final Codec<Optional<WithConditions<YLevelPlace>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	private final int minY;
	private final int maxY;
	@Nullable
	private final Optional<Identifier> dimensionLocation;

	public YLevelPlace(int minY, int maxY, Optional<Identifier> dimensionLocation, Identifier soundLocation, float volume, float pitch, String title,
	                   String subtitle, int duration, int fadeInDuration, int fadeOutDuration) {
		super(soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.minY = minY;
		this.maxY = maxY;
		this.dimensionLocation = dimensionLocation;
	}

	public YLevelPlace(int minY, int maxY, @Nullable Identifier dimensionLocation, Identifier soundLocation, float volume, float pitch, String title,
	                   String subtitle, int duration, int fadeInDuration, int fadeOutDuration) {
		this(minY, maxY, Optional.ofNullable(dimensionLocation), soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
	}

	public int minY() {
		return minY;
	}

	public int maxY() {
		return maxY;
	}

	public Optional<Identifier> dimensionLocation() {
		return dimensionLocation;
	}

	public int hashCode() {
		return Objects.hash(minY, maxY, dimensionLocation, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
	}

	@Override
	public String toString() {
		return "BasePlace[" +
				"minY=" + minY + ", " +
				"maxY=" + maxY + ", " +
				"dimension=" + dimensionLocation + ", " +
				"sound=" + soundLocation + ", " +
				"volume=" + volume + ", " +
				"pitch=" + pitch + ", " +
				"title=" + title + ", " +
				"subtitle=" + subtitle + ", " +
				"duration=" + duration + ", " +
				"fadeInDuration=" + fadeInDuration + ", " +
				"fadeOutDuration=" + fadeOutDuration + ", " + ']';
	}

	@Override
	public boolean matches(Player player) {
		boolean dimensionMatches = dimensionLocation() == null || player.level().dimension().identifier().equals(dimensionLocation());
		return player.getY() >= this.minY() && player.getY() <= this.maxY() && dimensionMatches;
	}

	@Override
	public PlaceType getType() {
		return PlaceType.Y_LEVEL;
	}
}
