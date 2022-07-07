package com.mrbysco.youarehere.registry.condition;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.config.HereConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnableBiomePlacesCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(YouAreHere.MOD_ID, "enable_biome_places");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		return HereConfig.COMMON.enableBiomePlaces.get();
	}

	public static class Serializer implements IConditionSerializer<EnableBiomePlacesCondition> {
		public static final EnableBiomePlacesCondition.Serializer INSTANCE = new EnableBiomePlacesCondition.Serializer();

		public void write(JsonObject json, EnableBiomePlacesCondition value) {

		}

		public EnableBiomePlacesCondition read(JsonObject json) {
			return new EnableBiomePlacesCondition();
		}

		public ResourceLocation getID() {
			return EnableBiomePlacesCondition.ID;
		}
	}
}
