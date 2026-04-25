package pl.konradowy.pierogis;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.codec.ByteBufCodecs;

public record BorderRadiusSyncPayload(int radius) implements CustomPacketPayload {
    public static final Type<BorderRadiusSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("pierogismod", "border_radius_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BorderRadiusSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, BorderRadiusSyncPayload::radius,
                    BorderRadiusSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}