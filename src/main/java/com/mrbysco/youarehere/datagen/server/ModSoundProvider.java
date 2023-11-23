package com.mrbysco.youarehere.datagen.server;

import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.registry.PlaceSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
	public ModSoundProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, YouAreHere.MOD_ID, helper);
	}

	@Override
	public void registerSounds() {
		this.add(PlaceSounds.SOUND_1, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_2, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_3, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_4, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_5, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_6, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_7, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_8, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_9, definition()
				.with(sound(modLoc("empty")))
		);
		this.add(PlaceSounds.SOUND_10, definition()
				.with(sound(modLoc("empty")))
		);
	}

	public ResourceLocation modLoc(String name) {
		return new ResourceLocation(YouAreHere.MOD_ID, name);
	}
}