package com.mrbysco.youarehere;

import com.mojang.logging.LogUtils;
import com.mrbysco.youarehere.config.HereConfig;
import com.mrbysco.youarehere.network.PacketHandler;
import com.mrbysco.youarehere.registry.PlaceSounds;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(YouAreHere.MOD_ID)
public class YouAreHere {
	public static final String MOD_ID = "youarehere";
	private static final Logger LOGGER = LogUtils.getLogger();

	public YouAreHere() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, HereConfig.commonSpec);

		eventBus.addListener(this::setup);

		PlaceTypeRegistry.PLACE_TYPE.register(eventBus);
		PlaceSounds.SOUND_EVENTS.register(eventBus);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}
}