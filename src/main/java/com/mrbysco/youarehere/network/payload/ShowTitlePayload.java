package com.mrbysco.youarehere.network.payload;

import com.mrbysco.youarehere.YouAreHere;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ShowTitlePayload(Identifier place, String placeType) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, ShowTitlePayload> CODEC = StreamCodec.composite(
			Identifier.STREAM_CODEC,
			ShowTitlePayload::place,
			ByteBufCodecs.STRING_UTF8,
			ShowTitlePayload::placeType,
			ShowTitlePayload::new);
	public static final Type<ShowTitlePayload> ID = new Type<>(YouAreHere.modLoc("show_title"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
