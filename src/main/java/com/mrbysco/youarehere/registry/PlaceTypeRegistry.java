package com.mrbysco.youarehere.registry;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.builder.ConditionalPlace;
import com.mrbysco.youarehere.registry.condition.EnableBiomePlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableDimensionPlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableYPlacesCondition;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import com.mrbysco.youarehere.resources.places.YPlace;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = YouAreHere.MOD_ID)
public class PlaceTypeRegistry {
	public static final ResourceKey<Registry<PlaceType<?>>> PLACE_TYPES = ResourceKey.createRegistryKey(new ResourceLocation(YouAreHere.MOD_ID, "place_types"));
	public static Supplier<IForgeRegistry<PlaceType<?>>> REGISTRY;

	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) {
		REGISTRY = event.create(new RegistryBuilder<PlaceType<?>>()
				.setName(new ResourceLocation(YouAreHere.MOD_ID, "place_types"))
		);
	}

	@SubscribeEvent
	public static void registerPlaceType(final RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(EnableYPlacesCondition.Serializer.INSTANCE);
			CraftingHelper.register(EnableBiomePlacesCondition.Serializer.INSTANCE);
			CraftingHelper.register(EnableDimensionPlacesCondition.Serializer.INSTANCE);
		}
		if (event.getRegistryKey().equals(PLACE_TYPES)) {
			event.register(PLACE_TYPES, new ResourceLocation(YouAreHere.MOD_ID, "conditional"), ConditionalPlace.Serializer::new);
		}
	}

	public static final DeferredRegister<PlaceType<?>> PLACE_TYPE = DeferredRegister.create(new ResourceLocation(YouAreHere.MOD_ID, "place_types"), YouAreHere.MOD_ID);

	public static final RegistryObject<PlaceType<?>> DIMENSION_TYPE = PLACE_TYPE.register("dimension", () -> new DimensionPlace.Serializer());
	public static final RegistryObject<PlaceType<?>> BIOME_TYPE = PLACE_TYPE.register("biome", () -> new BiomePlace.Serializer());
	public static final RegistryObject<PlaceType<?>> Y_LEVEL_TYPE = PLACE_TYPE.register("y_level", () -> new YPlace.Serializer());
}
