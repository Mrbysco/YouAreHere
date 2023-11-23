package com.mrbysco.youarehere.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class HereConfig {
	public static class Common {
		public final ModConfigSpec.BooleanValue enableBiomePlaces;
		public final ModConfigSpec.BooleanValue enableDimensionPlaces;
		public final ModConfigSpec.BooleanValue enableYPlaces;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("General settings").push("general");

			enableBiomePlaces = builder.comment("Dictates whether or not the vanilla biome places are enabled").define("enableBiomePlaces", true);

			enableDimensionPlaces = builder.comment("Dictates whether or not the vanilla dimension places are enabled (excluding the Overworld)").define("enableDimensionPlaces", true);

			enableYPlaces = builder.comment("Dictates whether or not the default y place for going below 0 is enabled").define("enableYPlaces", true);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
