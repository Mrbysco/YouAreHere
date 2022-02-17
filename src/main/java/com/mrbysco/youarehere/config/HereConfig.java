package com.mrbysco.youarehere.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import org.apache.commons.lang3.tuple.Pair;

public class HereConfig {
	public static class Common {
		public final BooleanValue enableBiomePlaces;
		public final BooleanValue enableDimensionPlaces;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("general");

			enableBiomePlaces = builder
					.comment("Dictates whether or not the vanilla biome places are enabled")
					.define("enableBiomePlaces", true);

			enableDimensionPlaces = builder
					.comment("Dictates whether or not the vanilla dimension places are enabled (excluding the Overworld)")
					.define("enableDimensionPlaces", true);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
