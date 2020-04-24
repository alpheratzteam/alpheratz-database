package pl.alpheratzteam.database.communication.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.api.packet.Packet;
import pl.alpheratzteam.database.api.packet.PacketBuffer;
import pl.alpheratzteam.database.api.packet.PacketDirection;
import java.util.List;
import java.util.Objects;

/**
 * @author hp888 on 20.04.2020.
 */

@RequiredArgsConstructor
public final class PacketDecoder extends ByteToMessageDecoder
{
    private final PacketDirection packetDirection;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        final int packetId = packetBuffer.readVarIntFromBuffer();
        final Packet packet = DatabaseDriver.INSTANCE.getPacketRegistry()
                .newInstance(packetDirection, packetId);

        if (Objects.isNull(packet))
            throw new DecoderException("Cannot decode unregistered packet! (" + packetId + ")");

        packet.read(packetBuffer);

        if (packet instanceof CallbackPacket && packetBuffer.isReadable()) {
            final CallbackPacket callbackPacket = (CallbackPacket) packet;
            callbackPacket.setCallbackId(packetBuffer.readLong());
            callbackPacket.setResponse(packetBuffer.readBoolean());
        }

        if (packetBuffer.isReadable())
            throw new DecoderException("Packet \"" + packet.getClass().getSimpleName() + "\" was larger than I expected!");

        list.add(packet);
    }
}