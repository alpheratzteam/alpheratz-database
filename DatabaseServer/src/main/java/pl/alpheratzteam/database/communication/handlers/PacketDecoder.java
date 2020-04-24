package pl.alpheratzteam.database.communication.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
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
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(bytes));
        final int packetId = packetBuffer.readVarIntFromBuffer();

        final Packet packet = DatabaseInitializer.INSTANCE.getPacketRegistry().newInstance(packetDirection, packetId);
        if (packet == null)
            throw new DecoderException("Cannot decode unregistered packet! (" + packetId + ")");

        packet.read(packetBuffer);

        if (byteBuf.readableBytes() > 0) {
            DatabaseInitializer.INSTANCE.getLogger().severe("Packet \"" + packet.getClass().getSimpleName() + "\" was larger than I expected!");
            byteBuf.skipBytes(byteBuf.readableBytes());
        }

        list.add(packet);
    }
}