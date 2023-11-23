package com.mrbysco.youarehere.registry.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.youarehere.config.HereConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class EnableDimensionPlacesCondition implements ICondition {

	public static final EnableDimensionPlacesCondition INSTANCE = new EnableDimensionPlacesCondition();

	public static final Codec<EnableDimensionPlacesCondition> CODEC = Codec.unit(INSTANCE).stable();

	private EnableDimensionPlacesCondition() {
	}

	@Override
	public boolean test(IContext context) {
		return HereConfig.COMMON.enableDimensionPlaces.get();
	}

	@Override
	public Codec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "default_dimension_enabled";
	}
}
