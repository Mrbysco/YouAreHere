package com.mrbysco.youarehere.registry.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.youarehere.config.HereConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class EnableYLevelPlaces implements ICondition {

	public static final EnableYLevelPlaces INSTANCE = new EnableYLevelPlaces();

	public static final Codec<EnableYLevelPlaces> CODEC = Codec.unit(INSTANCE).stable();

	private EnableYLevelPlaces() {
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
		return "default_y_level_enabled";
	}
}
