package com.mrbysco.youarehere.registry.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.youarehere.config.HereConfig;
import net.minecraft.util.StringRepresentable;

import java.util.function.BooleanSupplier;

public enum ConfigDefault implements StringRepresentable {
	BIOME("biome", HereConfig.COMMON.enableBiomePlaces::get),
	DIMENSION("dimension", HereConfig.COMMON.enableDimensionPlaces::get),
	Y_LEVEL("y_level", HereConfig.COMMON.enableYPlaces::get);

	public static final Codec<ConfigDefault> CODEC = StringRepresentable.fromEnum(ConfigDefault::values);
	private final String name;
	private final BooleanSupplier configSupplier;

	private ConfigDefault(String name, BooleanSupplier supplier) {
		this.name = name;
		this.configSupplier = supplier;
	}

	public boolean isEnabled() {
		return this.configSupplier.getAsBoolean();
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
