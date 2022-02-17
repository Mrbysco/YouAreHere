package com.mrbysco.youarehere.datagen;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import com.mrbysco.youarehere.datagen.provider.PlaceProvider;
import com.mrbysco.youarehere.datagen.provider.builder.BiomePlaceBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.ConditionalPlace;
import com.mrbysco.youarehere.datagen.provider.builder.DimensionPlaceBuilder;
import com.mrbysco.youarehere.datagen.provider.builder.YPlaceBuilder;
import com.mrbysco.youarehere.registry.condition.EnableBiomePlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableDimensionPlacesCondition;
import com.mrbysco.youarehere.registry.condition.EnableYPlacesCondition;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlaceDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();
		if (event.includeClient()) {
			generator.addProvider(new Places(generator));
		}
	}

	private static class Places extends PlaceProvider implements IConditionBuilder {
		public Places(DataGenerator generator) {
			super(generator, YouAreHere.MOD_ID);
		}

		@Override
		public void createPlaces(Consumer<FinishedPlace> consumer) {
			for(Biome biome : ForgeRegistries.BIOMES.getValues()) {
				ResourceLocation biomeLocation = biome.getRegistryName();
				ResourceLocation id = new ResourceLocation(YouAreHere.MOD_ID, "biome_" + biomeLocation.getPath());
				new ConditionalPlace.Builder()
						.addCondition(
								new EnableBiomePlacesCondition()
						)
						.addPlace(
								BiomePlaceBuilder.biome(biomeLocation)
										.setTitle("biome." + biomeLocation.getNamespace() + "." + biomeLocation.getPath())
										.setSoundLocation(SoundEvents.UI_TOAST_IN.getLocation())
										::save
						)
						.build(consumer, id);
			}

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableDimensionPlacesCondition()
					)
					.addPlace(
							DimensionPlaceBuilder.dimension(new ResourceLocation("the_nether"))
									.setTitle("youarehere.dimension.the_nether")
									.setSoundLocation(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation())
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "dimension_the_nether"));

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableDimensionPlacesCondition()
					)
					.addPlace(
							DimensionPlaceBuilder.dimension(new ResourceLocation("the_end"))
									.setTitle("youarehere.dimension.the_end")
									.setSoundLocation(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation())
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "dimension_the_end"));

			new ConditionalPlace.Builder()
					.addCondition(
							new EnableYPlacesCondition()
					)
					.addPlace(
							YPlaceBuilder.y(-64, 0)
									.setTitle("Below Zero")
									.setSoundLocation(SoundEvents.LAVA_POP.getLocation())
									::save
					)
					.build(consumer, new ResourceLocation(YouAreHere.MOD_ID, "y_below_zero"));
		}
	}
}
