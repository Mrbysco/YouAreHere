package com.mrbysco.youarehere.resources.places;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.condition.PlaceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Objects;
import java.util.Optional;

public class BiomePlace extends BasePlace {
	public static final ResourceKey<Registry<BiomePlace>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			new ResourceLocation(YouAreHere.MOD_ID, "biome"));
	public static final Codec<BiomePlace> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
			RecordCodecBuilder.create(
					apply -> apply.group(
									ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomePlace::biomeLocation),
									ResourceLocation.CODEC.fieldOf("sound").forGetter(BiomePlace::soundLocation),
									ExtraCodecs.strictOptionalField(Codec.FLOAT, "volume", 1.0F).forGetter(BiomePlace::getVolume),
									ExtraCodecs.strictOptionalField(Codec.FLOAT, "pitch", 1.0F).forGetter(BiomePlace::getPitch),
									Codec.STRING.fieldOf("title").forGetter(BiomePlace::title),
									ExtraCodecs.strictOptionalField(Codec.STRING, "subtitle", "").forGetter(BiomePlace::subtitle),
									ExtraCodecs.strictOptionalField(Codec.INT, "duration", 20).forGetter(BiomePlace::duration),
									ExtraCodecs.strictOptionalField(Codec.INT, "fadeInDuration", 20).forGetter(BiomePlace::fadeInDuration),
									ExtraCodecs.strictOptionalField(Codec.INT, "fadeOutDuration", 20).forGetter(BiomePlace::fadeOutDuration)
							)
							.apply(apply, BiomePlace::new)
			)
	);

	public static final Codec<Optional<WithConditions<BiomePlace>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	private final ResourceLocation biomeLocation;

	public BiomePlace(ResourceLocation biomeLocation, ResourceLocation soundLocation, float volume, float pitch, String title,
					  String subtitle, int duration, int fadeInDuration, int fadeOutDuration) {
		super(soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
		this.biomeLocation = biomeLocation;
	}

	public ResourceLocation biomeLocation() {
		return biomeLocation;
	}

	public int hashCode() {
		return Objects.hash(biomeLocation, soundLocation, volume, pitch, title, subtitle, duration, fadeInDuration, fadeOutDuration);
	}

	@Override
	public String toString() {
		return "BiomePlace[" +
				"biome=" + biomeLocation + ", " +
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
		BlockPos pos = player.blockPosition();
		ResourceKey<Biome> biomeKey = player.level().getBiome(pos).unwrapKey().orElse(null);
		if (biomeKey == null) {
			return false;
		}
		ResourceLocation biomeID = biomeKey.location();
		return biomeID != null && biomeID.equals(this.biomeLocation());
	}

	@Override
	public PlaceType getType() {
		return PlaceType.BIOME;
	}
}
