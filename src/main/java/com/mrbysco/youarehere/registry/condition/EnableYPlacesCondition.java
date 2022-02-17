package com.mrbysco.youarehere.registry.condition;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.config.HereConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnableYPlacesCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(YouAreHere.MOD_ID, "enable_y_places");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test() {
		return HereConfig.COMMON.enableBiomePlaces.get();
	}

	public static class Serializer implements IConditionSerializer<EnableYPlacesCondition> {
		public static final EnableYPlacesCondition.Serializer INSTANCE = new EnableYPlacesCondition.Serializer();

		public void write(JsonObject json, EnableYPlacesCondition value) {

		}

		public EnableYPlacesCondition read(JsonObject json) {
			return new EnableYPlacesCondition();
		}

		public ResourceLocation getID() {
			return EnableYPlacesCondition.ID;
		}
	}
}
