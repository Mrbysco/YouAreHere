package com.mrbysco.youarehere.datagen.provider;

import com.google.gson.JsonObject;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import com.mrbysco.youarehere.resources.places.PlaceType;
import net.minecraft.resources.ResourceLocation;

public interface FinishedPlace {
	void serializePlaceData(JsonObject jsonObject);

	default JsonObject serializePlace() {
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("type", PlaceTypeRegistry.REGISTRY.get().getKey(this.getType()).toString());
		this.serializePlaceData(jsonobject);
		return jsonobject;
	}

	ResourceLocation getId();

	PlaceType<?> getType();
}
