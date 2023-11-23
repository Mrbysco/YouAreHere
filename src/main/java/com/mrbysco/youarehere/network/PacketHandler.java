package com.mrbysco.youarehere.network;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.packet.ShowTitleMessage;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(YouAreHere.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void init() {
//		CHANNEL.registerMessage(id++, UpdatePlacesMessage.class, UpdatePlacesMessage::encode, UpdatePlacesMessage::decode, UpdatePlacesMessage::handle);
		CHANNEL.registerMessage(id++, ShowTitleMessage.class, ShowTitleMessage::encode, ShowTitleMessage::decode, ShowTitleMessage::handle);
	}
}
