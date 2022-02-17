package com.mrbysco.youarehere.datagen.provider.builder;

import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface PlaceBuilder {
	ResourceLocation getDefaultID();

	void save(Consumer<FinishedPlace> placeConsumer, ResourceLocation location);

	default void save(Consumer<FinishedPlace> placeConsumer) {
		this.save(placeConsumer, getDefaultID());
	}

	default void save(Consumer<FinishedPlace> placeConsumer, String s) {
		ResourceLocation resourcelocation = getDefaultID();
		ResourceLocation resourcelocation1 = new ResourceLocation(s);
		if (resourcelocation1.equals(resourcelocation)) {
			throw new IllegalStateException("Place " + s + " should remove its 'save' argument as it is equal to default one");
		} else {
			this.save(placeConsumer, resourcelocation1);
		}
	}
}
