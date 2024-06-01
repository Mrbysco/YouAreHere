package com.mrbysco.youarehere.network;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.handler.ClientPayloadHandler;
import com.mrbysco.youarehere.network.payload.ShowTitlePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(YouAreHere.MOD_ID);

		registrar.playToClient(ShowTitlePayload.ID, ShowTitlePayload.CODEC, ClientPayloadHandler.getInstance()::handleData);
	}
}
