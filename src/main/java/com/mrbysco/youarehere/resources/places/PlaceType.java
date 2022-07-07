package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface PlaceType<T extends BasePlace> {
	T fromJson(ResourceLocation location, JsonObject jsonObject);

	@Nullable
	T fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf);

	void toNetwork(FriendlyByteBuf byteBuf, T place);
}
