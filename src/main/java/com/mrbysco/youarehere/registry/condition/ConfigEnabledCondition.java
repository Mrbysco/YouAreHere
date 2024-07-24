package com.mrbysco.youarehere.registry.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ConfigEnabledCondition(ConfigDefault config) implements ICondition {

	public static final MapCodec<ConfigEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(
			builder -> builder
					.group(
							ConfigDefault.CODEC.fieldOf("config").forGetter(ConfigEnabledCondition::config))
					.apply(builder, ConfigEnabledCondition::new));

	@Override
	public boolean test(IContext context) {
		return config.isEnabled();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "config_enabled";
	}
}
