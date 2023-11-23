package com.mrbysco.youarehere;

import com.mojang.logging.LogUtils;
import com.mrbysco.youarehere.config.HereConfig;
import com.mrbysco.youarehere.network.PacketHandler;
import com.mrbysco.youarehere.registry.PlaceSounds;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(YouAreHere.MOD_ID)
public class YouAreHere {
	public static final String MOD_ID = "youarehere";
	public static final Logger LOGGER = LogUtils.getLogger();

	public YouAreHere() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, HereConfig.commonSpec);

		PlaceTypeRegistry.CONDITION_CODECS.register(eventBus);

		eventBus.addListener(PlaceTypeRegistry::onNewRegistry);
		eventBus.addListener(this::setup);

		PlaceSounds.SOUND_EVENTS.register(eventBus);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}
}