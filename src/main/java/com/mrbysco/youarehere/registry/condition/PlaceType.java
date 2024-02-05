package com.mrbysco.youarehere.registry.condition;

public enum PlaceType {
	BIOME("biome"),
	DIMENSION("dimension"),
	Y_LEVEL("y_level");

	private final String name;

	PlaceType(String type) {
		this.name = type;
	}

	public String getName() {
		return name;
	}

	public static PlaceType getFromName(String name) {
		for (PlaceType type : values()) {
			if (type.name.equalsIgnoreCase(name)) { //Just to be safe
				return type;
			}
		}
		return BIOME;
	}
}
