package com.mrbysco.youarehere.datagen.provider.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mrbysco.youarehere.datagen.provider.FinishedPlace;
import com.mrbysco.youarehere.resources.PlaceManager;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Borrowed from net.minecraftforge.common.crafting.ConditionalRecipe
 */
public class ConditionalPlace {
	@ObjectHolder(registryName = "youarehere:place_types", value = "youarehere:conditional")
	public static final PlaceType<BasePlace> PLACE_TYPE = null;

	public static ConditionalPlace.Builder builder() {
		return new ConditionalPlace.Builder();
	}

	public static class Serializer<T extends BasePlace> implements PlaceType<T> {

		@SuppressWarnings("unchecked") // We return a nested one, so we can't know what type it is.
		@Override
		public T fromJson(ResourceLocation placeId, JsonObject json) {
			JsonArray items = GsonHelper.getAsJsonArray(json, "places");
			int idx = 0;
			for (JsonElement ele : items) {
				if (!ele.isJsonObject())
					throw new JsonSyntaxException("Invalid places entry at index " + idx + " Must be JsonObject");
				if (CraftingHelper.processConditions(GsonHelper.getAsJsonArray(ele.getAsJsonObject(), "conditions"), IContext.EMPTY))
					return (T) PlaceManager.fromJson(placeId, GsonHelper.getAsJsonObject(ele.getAsJsonObject(), "place"));
				idx++;
			}
			return null;
		}

		//Should never get here as we return one of the places we wrap.
		@Override
		public T fromNetwork(ResourceLocation placeId, FriendlyByteBuf buffer) {
			return null;
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, T place) {
		}
	}

	public static class Builder {
		private List<ICondition[]> conditions = new ArrayList<>();
		private List<FinishedPlace> places = new ArrayList<>();

		private List<ICondition> currentConditions = new ArrayList<>();

		public ConditionalPlace.Builder addCondition(ICondition condition) {
			currentConditions.add(condition);
			return this;
		}

		public ConditionalPlace.Builder addPlace(Consumer<Consumer<FinishedPlace>> callable) {
			callable.accept(this::addPlace);
			return this;
		}

		public ConditionalPlace.Builder addPlace(FinishedPlace place) {
			if (currentConditions.isEmpty())
				throw new IllegalStateException("Can not add a place with no conditions.");
			conditions.add(currentConditions.toArray(new ICondition[currentConditions.size()]));
			places.add(place);
			currentConditions.clear();
			return this;
		}

		public void build(Consumer<FinishedPlace> consumer, String namespace, String path) {
			build(consumer, new ResourceLocation(namespace, path));
		}

		public void build(Consumer<FinishedPlace> consumer, ResourceLocation id) {
			if (!currentConditions.isEmpty())
				throw new IllegalStateException("Invalid ConditionalPlace builder, Orphaned conditions");
			if (places.isEmpty())
				throw new IllegalStateException("Invalid ConditionalPlace builder, No Places setup");

			consumer.accept(new Finished(id, conditions, places));
		}
	}

	private static class Finished implements FinishedPlace {
		private final ResourceLocation id;
		private final List<ICondition[]> conditions;
		private final List<FinishedPlace> places;

		private Finished(ResourceLocation id, List<ICondition[]> conditions, List<FinishedPlace> places) {
			this.id = id;
			this.conditions = conditions;
			this.places = places;
		}

		@Override
		public void serializePlaceData(JsonObject json) {
			JsonArray array = new JsonArray();
			json.add("places", array);
			for (int x = 0; x < conditions.size(); x++) {
				JsonObject holder = new JsonObject();

				JsonArray conds = new JsonArray();
				for (ICondition c : conditions.get(x))
					conds.add(CraftingHelper.serialize(c));
				holder.add("conditions", conds);
				holder.add("place", places.get(x).serializePlace());

				array.add(holder);
			}
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public PlaceType<?> getType() {
			return PLACE_TYPE;
		}
	}
}
