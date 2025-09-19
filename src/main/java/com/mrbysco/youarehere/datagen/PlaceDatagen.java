package com.mrbysco.youarehere.datagen;

import com.mrbysco.youarehere.datagen.client.ModLanguageProvider;
import com.mrbysco.youarehere.datagen.client.ModSoundProvider;
import com.mrbysco.youarehere.datagen.server.ModPlaceProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class PlaceDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(true, new ModLanguageProvider(packOutput));
		generator.addProvider(true, new ModSoundProvider(packOutput));

		generator.addProvider(true, new ModPlaceProvider(packOutput, event.getLookupProvider()));

	}
}
