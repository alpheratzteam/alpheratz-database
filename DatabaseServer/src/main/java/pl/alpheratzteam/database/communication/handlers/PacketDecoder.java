package pl.alpheratzteam.database.communication.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
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
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        final int packetId = packetBuffer.readInt();
        final Packet packet = DatabaseInitializer.INSTANCE.getPacketRegistry()
                .newInstance(packetDirection, packetId);

        if (Objects.isNull(packet))
            throw new DecoderException("Received invalid packet! (id=" + packetId + ")");

        packet.read(packetBuffer);

        if (packet instanceof CallbackPacket) {
            final CallbackPacket callbackPacket = (CallbackPacket) packet;
            callbackPacket.setCallbackId(packetBuffer.readLong());
            callbackPacket.setResponse(packetBuffer.readBoolean());
        }

        list.add(packet);
    }
}