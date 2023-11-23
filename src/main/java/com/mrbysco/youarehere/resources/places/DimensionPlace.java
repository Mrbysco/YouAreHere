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

import java.util.Objects;
import java.util.Optional;

public class DimensionPlace extends BasePlace {
	public static final ResourceKey<Registry<DimensionPlace>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			new ResourceLocation(YouAreHere.MOD_ID, "dimension"));
	public static final Codec<DimensionPlace> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
			RecordCodecBuilder.create(
					apply -> apply.group(
									ResourceLocation.CODEC.fieldOf("dimension").forGetter(DimensionPlace::dimensionLocation),
									ResourceLocation.CODEC.fieldOf("sound").forGetter(BasePlace::soundLocation),
									ExtraCodecs.strictOptionalField(Codec.FLOAT, "volume", 1.0F).forGetter(BasePlace::getVolume),
									ExtraCodecs.strictOptionalField(Codec.FLOAT, "pitch", 1.0F).forGetter(BasePlace::getPitch),
									Codec.STRING.fieldOf("title").forGetter(BasePlace::title),
									ExtraCodecs.strictOptionalField(Codec.STRING, "subtitle", "").forGetter(BasePlace::subtitle),
									ExtraCodecs.strictOptionalField(Codec.INT, "duration", 20).forGetter(BasePlace::duration),
									ExtraCodecs.strictOptionalField(Codec.INT, "fadeInDuration", 20).forGetter(BasePlace::fadeInDuration),
									ExtraCodecs.strictOptionalField(Codec.INT, "fadeOutDuration", 20).forGetter(BasePlace::fadeOutDuration)
							)
							.apply(apply, DimensionPlace::new)
			)
	);
	public static final Codec<Optional<WithConditions<DimensionPlace>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	private final ResourceLocation dimensionLocation;

	public DimensionPlace(ResourceLocation dimensionLocation, ResourceLocation soundLocation, float volume, float pitch, String title,
						  String subtitle, int duration, int fadeInDuration, int fadeOutDuration) {
		super(soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.dimensionLocation = dimensionLocation;
	}

	public ResourceLocation dimensionLocation() {
		return dimensionLocation;
	}

	public int hashCode() {
		return Objects.hash(dimensionLocation, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
	}

	@Override
	public String toString() {
		return "BasePlace[" +
				"dimension=" + dimensionLocation + ", " +
				"sound=" + soundLocation + ", " +
				"volume=" + volume + ", " +
				"pitch=" + pitch + ", " +
				"title=" + title + ", " +
				"subtitle=" + subtitle + ", " +
				"duration=" + duration + ", " +
				"fadeInDuration=" + fadeInDuration + ", " +
				"fadeOutDuration=" + fadeOutDuration + ']';
	}

	@Override
	public boolean matches(Player player) {
		return player.level().dimension().location().equals(this.dimensionLocation());
	}

	@Override
	public PlaceType getType() {
		return PlaceType.DIMENSION;
	}
}
