package com.mrbysco.youarehere.datagen;

import com.mrbysco.youarehere.datagen.server.ModPlaceProvider;
import com.mrbysco.youarehere.datagen.server.ModSoundProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Bus.MOD)
public class PlaceDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new ModPlaceProvider(packOutput, event.getLookupProvider()));
			generator.addProvider(event.includeClient(), new ModSoundProvider(packOutput, helper));
		}
	}
}
