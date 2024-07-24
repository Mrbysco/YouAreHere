package com.mrbysco.youarehere.network.payload;

import com.mrbysco.youarehere.YouAreHere;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ShowTitlePayload(ResourceLocation place, String placeType) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, ShowTitlePayload> CODEC = CustomPacketPayload.codec(
			ShowTitlePayload::write,
			ShowTitlePayload::new);
	public static final Type<ShowTitlePayload> ID = new Type<>(YouAreHere.modLoc("show_title"));


	public ShowTitlePayload(final FriendlyByteBuf buffer) {
		this(buffer.readResourceLocation(), buffer.readUtf());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.place);
		buffer.writeUtf(this.placeType);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
