package com.mrbysco.youarehere.resources.places;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public interface PlaceType<T extends BasePlace> extends IForgeRegistryEntry<PlaceType<?>> {
	T fromJson(ResourceLocation location, JsonObject jsonObject);

	@Nullable
	T fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf);

	void toNetwork(FriendlyByteBuf byteBuf, T place);
}
