package com.mrbysco.youarehere.network.packet;

import com.google.common.collect.Lists;
import com.mrbysco.youarehere.registry.PlaceTypeRegistry;
import com.mrbysco.youarehere.resources.PlaceManager;
import com.mrbysco.youarehere.resources.places.BasePlace;
import com.mrbysco.youarehere.resources.places.PlaceType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent.Context;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class UpdatePlacesMessage {
	private final List<BasePlace> places;

	public UpdatePlacesMessage(Collection<BasePlace> places) {
		this.places = Lists.newArrayList(places);
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeCollection(this.places, UpdatePlacesMessage::toNetwork);
	}

	public static <T extends BasePlace> void toNetwork(FriendlyByteBuf buffer, T value) {
		buffer.writeResourceLocation(PlaceTypeRegistry.REGISTRY.get().getKey(value.getType()));
		buffer.writeResourceLocation(value.id());
		value.getType().toNetwork(buffer, value);
	}

	public static UpdatePlacesMessage decode(final FriendlyByteBuf buffer) {
		List<BasePlace> places = buffer.readList(UpdatePlacesMessage::fromNetwork);
		return new UpdatePlacesMessage(places);
	}

	public static BasePlace fromNetwork(FriendlyByteBuf buffer) {
		ResourceLocation resourcelocation = buffer.readResourceLocation();
		ResourceLocation resourcelocation1 = buffer.readResourceLocation();
		PlaceType<?> type = PlaceTypeRegistry.REGISTRY.get().getValue(resourcelocation);
		if (type == null) {
			throw new IllegalArgumentException("Unknown place serializer " + resourcelocation);
		} else {
			return type.fromNetwork(resourcelocation1, buffer);
		}
	}

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				UpdateMaterials.update(this.places).run();
			}
		});
		ctx.setPacketHandled(true);
	}

	private static class UpdateMaterials {
		private static SafeRunnable update(List<BasePlace> places) {
			return new SafeRunnable() {
				@Serial
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					PlaceManager.INSTANCE.replacePlaces(places);
				}
			};
		}
	}
}
