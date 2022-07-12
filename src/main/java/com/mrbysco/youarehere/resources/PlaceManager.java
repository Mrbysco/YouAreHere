package com.mrbysco.youarehere.resources;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mrbysco.youarehere.YouAreHere;
import com.mrbysco.youarehere.network.PacketHandler;
import com.mrbysco.youarehere.network.packet.UpdatePlacesMessage;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EventBusSubscriber(modid = YouAreHere.MOD_ID, bus = Bus.FORGE)
public class PlaceManager extends SimpleJsonResourceReloadListener {
	public static final String FOLDER_NAME = "places";
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();

	public static final PlaceManager INSTANCE = new PlaceManager();

	private Map<PlaceType<?>, Map<ResourceLocation, BasePlace>> places = ImmutableMap.of();
	private Map<ResourceLocation, BasePlace> byName = ImmutableMap.of();

	public PlaceManager() {
		super(GSON, FOLDER_NAME);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> elementMap, ResourceManager manager, ProfilerFiller profilerFiller) {
		Map<PlaceType<?>, Builder<ResourceLocation, BasePlace>> map = Maps.newHashMap();
		Builder<ResourceLocation, BasePlace> builder = ImmutableMap.builder();
		for (Map.Entry<ResourceLocation, JsonElement> entry : elementMap.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			if (resourcelocation.getPath().startsWith("_"))
				continue; //Forge: filter anything beginning with "_" as it's used for metadata.

			try {
				if (entry.getValue().isJsonObject() && !net.minecraftforge.common.crafting.CraftingHelper.processConditions(entry.getValue().getAsJsonObject(), "conditions", IContext.EMPTY)) {
					LOGGER.debug("Skipping loading place info {} as it's conditions were not met", resourcelocation);
					continue;
				}
				BasePlace basePlace = fromJson(resourcelocation, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
				if (basePlace == null) {
					LOGGER.info("Skipping loading Place Info {} as it's serializer returned null", resourcelocation);
					continue;
				}
				map.computeIfAbsent(basePlace.getType(), (p_44075_) -> {
					return ImmutableMap.builder();
				}).put(resourcelocation, basePlace);
				builder.put(resourcelocation, basePlace);
			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				LOGGER.error("Parsing error loading Base Info {}", resourcelocation, jsonparseexception);
			}
		}
		this.places = map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (builderEntry) -> {
			return builderEntry.getValue().build();
		}));
		this.byName = builder.build();
		LOGGER.info("Loaded {} Place Info", byName.size());
	}

	public static BasePlace fromJson(ResourceLocation location, JsonObject jsonObject) {
		String s = GsonHelper.getAsString(jsonObject, "type");
		PlaceType<? extends BasePlace> placeType = PlaceTypeRegistry.REGISTRY.get().getValue(new ResourceLocation(s));
		if (placeType == null) {
			throw new JsonSyntaxException("Invalid or unsupported place type '" + s + "'");
		} else {
			return placeType.fromJson(location, jsonObject);
		}
	}

	public void replacePlaces(Iterable<BasePlace> basePlaces) {
		Map<PlaceType<?>, Map<ResourceLocation, BasePlace>> map = Maps.newHashMap();
		Builder<ResourceLocation, BasePlace> builder = ImmutableMap.builder();
		basePlaces.forEach((place) -> {
			Map<ResourceLocation, BasePlace> map1 = map.computeIfAbsent(place.getType(), (type) -> {
				return Maps.newHashMap();
			});
			ResourceLocation resourcelocation = place.id();
			BasePlace recipe = map1.put(resourcelocation, place);
			builder.put(resourcelocation, place);
			if (recipe != null) {
				throw new IllegalStateException("Duplicate recipe ignored with ID " + resourcelocation);
			}
		});
		this.places = ImmutableMap.copyOf(map);
		this.byName = builder.build();
	}

	@SubscribeEvent
	public static void addReloadListener(AddReloadListenerEvent event) {
		event.addListener(INSTANCE);
	}

	@SubscribeEvent
	public static void syncPlaces(PlayerLoggedInEvent event) {
		if (!event.getEntity().level.isClientSide)
			INSTANCE.syncToPlayer((ServerPlayer) event.getEntity());

		if (ServerLifecycleHooks.getCurrentServer() != null)
			INSTANCE.syncToAll();
	}

	private void syncToAll() {
		PacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdatePlacesMessage(this.getPlaces()));
	}

	private void syncToPlayer(ServerPlayer player) {
		PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new UpdatePlacesMessage(this.getPlaces()));
	}

	public <T extends BasePlace> List<T> getAllRecipesFor(PlaceType<T> placeType) {
		return this.byType(placeType).values().stream().map((place) -> {
			return (T) place;
		}).collect(Collectors.toList());
	}

	private <T extends BasePlace> Map<ResourceLocation, BasePlace> byType(PlaceType<T> placeType) {
		return (Map<ResourceLocation, BasePlace>) (Map<ResourceLocation, T>) this.places.getOrDefault(placeType, Collections.emptyMap());
	}

	public Optional<? extends BasePlace> byKey(ResourceLocation location) {
		return Optional.ofNullable(this.byName.get(location));
	}

	public Collection<BasePlace> getPlaces() {
		return this.places.values().stream().flatMap((placeMap) -> {
			return placeMap.values().stream();
		}).collect(Collectors.toSet());
	}

	public Stream<ResourceLocation> getPlaceIds() {
		return this.places.values().stream().flatMap((placeMap) -> {
			return placeMap.keySet().stream();
		});
	}
}
