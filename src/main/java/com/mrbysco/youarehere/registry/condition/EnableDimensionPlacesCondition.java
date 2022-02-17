package com.mrbysco.youarehere.registry.condition;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.config.HereConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnableDimensionPlacesCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(YouAreHere.MOD_ID, "enable_dimension_places");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test() {
		return HereConfig.COMMON.enableDimensionPlaces.get();
	}

	public static class Serializer implements IConditionSerializer<EnableDimensionPlacesCondition> {
		public static final EnableDimensionPlacesCondition.Serializer INSTANCE = new EnableDimensionPlacesCondition.Serializer();

		public void write(JsonObject json, EnableDimensionPlacesCondition value) {

		}

		public EnableDimensionPlacesCondition read(JsonObject json) {
			return new EnableDimensionPlacesCondition();
		}

		public ResourceLocation getID() {
			return EnableDimensionPlacesCondition.ID;
		}
	}
}
