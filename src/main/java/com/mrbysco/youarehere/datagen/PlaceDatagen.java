package com.mrbysco.youarehere.datagen;

import com.mrbysco.youarehere.datagen.client.ModLanguageProvider;
import com.mrbysco.youarehere.datagen.client.ModSoundProvider;
import com.mrbysco.youarehere.datagen.server.ModPlaceProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class PlaceDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(ModLanguageProvider::new);
		event.createProvider(ModSoundProvider::new);
		event.createProvider(ModPlaceProvider::new);
	}
}
