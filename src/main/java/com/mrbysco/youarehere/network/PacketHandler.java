package com.mrbysco.youarehere.network;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.handler.ClientPayloadHandler;
import com.mrbysco.youarehere.network.payload.ShowTitlePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {
	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(YouAreHere.MOD_ID);

		registrar.play(ShowTitlePayload.ID, ShowTitlePayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleData));
	}
}
