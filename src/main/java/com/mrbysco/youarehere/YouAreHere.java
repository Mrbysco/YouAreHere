package com.mrbysco.youarehere;

import com.mojang.logging.LogUtils;
import com.mrbysco.youarehere.config.HereConfig;
import com.mrbysco.youarehere.network.PacketHandler;
import com.mrbysco.youarehere.registry.PlaceSounds;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(YouAreHere.MOD_ID)
public class YouAreHere {
	public static final String MOD_ID = "youarehere";
	public static final Logger LOGGER = LogUtils.getLogger();

	public YouAreHere(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(Type.COMMON, HereConfig.commonSpec);

		PlaceTypeRegistry.CONDITION_CODECS.register(eventBus);

		eventBus.addListener(PlaceTypeRegistry::onNewRegistry);
		eventBus.addListener(PacketHandler::setupPackets);

		PlaceSounds.SOUND_EVENTS.register(eventBus);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}