package com.mrbysco.youarehere.network.payload;

import com.mrbysco.youarehere.YouAreHere;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ShowTitlePayload(ResourceLocation place, String type) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(YouAreHere.MOD_ID, "show_title");

	public ShowTitlePayload(final FriendlyByteBuf buffer) {
		this(buffer.readResourceLocation(), buffer.readUtf());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.place);
		buffer.writeUtf(this.type);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
