package com.mrbysco.youarehere.resources.places;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.condition.PlaceType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class YLevelPlace extends BasePlace {
	public static final ResourceKey<Registry<YLevelPlace>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			new ResourceLocation(YouAreHere.MOD_ID, "y_level"));
	public static final Codec<YLevelPlace> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
			RecordCodecBuilder.create(
					apply -> apply.group(
									Codec.INT.fieldOf("minY").forGetter(YLevelPlace::minY),
									Codec.INT.fieldOf("maxY").forGetter(YLevelPlace::maxY),
									ResourceLocation.CODEC.fieldOf("dimension").forGetter(YLevelPlace::dimensionLocation),
									ResourceLocation.CODEC.fieldOf("sound").forGetter(BasePlace::soundLocation),
									ExtraCodecs.strictOptionalField(Codec.FLOAT, "volume", 1.0F).forGetter(BasePlace::getVolume),
									ExtraCodecs.strictOptionalField(Codec.FLOAT, "pitch", 1.0F).forGetter(BasePlace::getPitch),
									Codec.STRING.fieldOf("title").forGetter(BasePlace::title),
									ExtraCodecs.strictOptionalField(Codec.STRING, "subtitle", "").forGetter(BasePlace::subtitle),
									ExtraCodecs.strictOptionalField(Codec.INT, "duration", 20).forGetter(BasePlace::duration),
									ExtraCodecs.strictOptionalField(Codec.INT, "fadeInDuration", 20).forGetter(BasePlace::fadeInDuration),
									ExtraCodecs.strictOptionalField(Codec.INT, "fadeOutDuration", 20).forGetter(BasePlace::fadeOutDuration)
							)
							.apply(apply, YLevelPlace::new)
			)
	);
	public static final Codec<Optional<WithConditions<YLevelPlace>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	private final int minY;
	private final int maxY;
	@Nullable
	private final ResourceLocation dimensionLocation;

	public YLevelPlace(int minY, int maxY, ResourceLocation dimensionLocation, ResourceLocation soundLocation, float volume, float pitch, String title,
					   String subtitle, int duration, int fadeInDuration, int fadeOutDuration) {
		super(soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
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

	public ResourceLocation dimensionLocation() {
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
		boolean dimensionMatches = dimensionLocation() == null || player.level().dimension().location().equals(dimensionLocation());
		return player.getY() >= this.minY() && player.getY() <= this.maxY() && dimensionMatches;
	}

	@Override
	public PlaceType getType() {
		return PlaceType.Y_LEVEL;
	}
}
