package com.mrbysco.youarehere.registry;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.builder.ConditionalPlace;
import com.mrbysco.youarehere.registry.condition.EnableBiomePlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableDimensionPlacesCondition;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.resources.places.BiomePlace;
import com.mrbysco.youarehere.resources.places.DimensionPlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import com.mrbysco.youarehere.resources.places.YPlace;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = YouAreHere.MOD_ID)
public class PlaceTypeRegistry {
	public static IForgeRegistry<PlaceType<?>> REGISTRY;

	@SubscribeEvent
	public static void onNewRegistry(RegistryEvent.NewRegistry event) {
		REGISTRY = new RegistryBuilder<PlaceType<?>>()
				.setName(new ResourceLocation(YouAreHere.MOD_ID, "place_types"))
				.setType(c(PlaceType.class))
				.create();
	}

	@SubscribeEvent
	public static void registerPlaceType(final RegistryEvent.Register<PlaceType<?>> event) {
		IForgeRegistry<PlaceType<?>> registry = event.getRegistry();

		CraftingHelper.register(EnableBiomePlacesCondition.Serializer.INSTANCE);
		CraftingHelper.register(EnableDimensionPlacesCondition.Serializer.INSTANCE);

		event.getRegistry().register(new ConditionalPlace.Serializer<BasePlace>().setRegistryName(new ResourceLocation(YouAreHere.MOD_ID, "conditional")));
	}

	private static <T> Class<T> c(Class<?> cls) { return (Class<T>)cls; }

	public static final DeferredRegister<PlaceType<?>> PLACE_TYPE = DeferredRegister.create((Class)PlaceType.class, YouAreHere.MOD_ID);

	public static final RegistryObject<PlaceType<?>> DIMENSION_TYPE = PLACE_TYPE.register("dimension", () -> new DimensionPlace.Serializer());
	public static final RegistryObject<PlaceType<?>> BIOME_TYPE = PLACE_TYPE.register("biome", () -> new BiomePlace.Serializer());
	public static final RegistryObject<PlaceType<?>> Y_LEVEL_TYPE = PLACE_TYPE.register("y_level", () -> new YPlace.Serializer());
}
