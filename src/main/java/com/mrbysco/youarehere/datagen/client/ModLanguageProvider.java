package com.mrbysco.youarehere.datagen.client;

import com.mrbysco.youarehere.YouAreHere;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(PackOutput output) {
		super(output, YouAreHere.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("youarehere.dimension.the_end", "The End");
		add("youarehere.dimension.the_nether", "The Nether");
		add("youarehere.dimension.below_zero", "Below Zero");
		add("youarehere.dimension.above_ceiling", "Above Ceiling");
		add("youarehere.networking.show_title.failed", "Failed to show title %s");
		addConfig("title", "You Are Here Config", null);
		addConfig("general", "General", "General settings");
		addConfig("enableBiomePlaces", "Enable Biome Places", "Dictates whether or not the vanilla biome places are enabled");
		addConfig("enableDimensionPlaces", "Enable Dimension Places", "Dictates whether or not the vanilla dimension places are enabled (excluding the Overworld)");
		addConfig("enableYPlaces", "Enable Y Places", "Dictates whether or not the default y place for going below 0 is enabled");
	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add("youarehere.configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add("youarehere.configuration." + path + ".tooltip", description);
	}
}
