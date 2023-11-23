package com.mrbysco.youarehere.registry.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.youarehere.config.HereConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class EnableBiomePlacesCondition implements ICondition {

	public static final EnableBiomePlacesCondition INSTANCE = new EnableBiomePlacesCondition();

	public static final Codec<EnableBiomePlacesCondition> CODEC = Codec.unit(INSTANCE).stable();

	private EnableBiomePlacesCondition() {
	}

	@Override
	public boolean test(IContext context) {
		return HereConfig.COMMON.enableBiomePlaces.get();
	}

	@Override
	public Codec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "default_biome_enabled";
	}
}
