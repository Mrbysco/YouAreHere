package com.mrbysco.youarehere.registry;

import com.mrbysco.youarehere.YouAreHere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PlaceSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, YouAreHere.MOD_ID);

	public static final RegistryObject<SoundEvent> SOUND_1 = SOUND_EVENTS.register("sound.1", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.1")));
	public static final RegistryObject<SoundEvent> SOUND_2 = SOUND_EVENTS.register("sound.2", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.2")));
	public static final RegistryObject<SoundEvent> SOUND_3 = SOUND_EVENTS.register("sound.3", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.3")));
	public static final RegistryObject<SoundEvent> SOUND_4 = SOUND_EVENTS.register("sound.4", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.4")));
	public static final RegistryObject<SoundEvent> SOUND_5 = SOUND_EVENTS.register("sound.5", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.5")));
	public static final RegistryObject<SoundEvent> SOUND_6 = SOUND_EVENTS.register("sound.6", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.6")));
	public static final RegistryObject<SoundEvent> SOUND_7 = SOUND_EVENTS.register("sound.7", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.7")));
	public static final RegistryObject<SoundEvent> SOUND_8 = SOUND_EVENTS.register("sound.8", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.8")));
	public static final RegistryObject<SoundEvent> SOUND_9 = SOUND_EVENTS.register("sound.9", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.9")));
	public static final RegistryObject<SoundEvent> SOUND_10 = SOUND_EVENTS.register("sound.10", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(YouAreHere.MOD_ID, "sound.10")));
}
