package me.hp888.database.communication.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.packet.Packet;
import me.hp888.database.api.packet.PacketBuffer;
import me.hp888.database.api.packet.PacketDirection;
import java.util.List;

/**
 * @author hp888 on 20.04.2020.
 */

@RequiredArgsConstructor
public final class PacketDecoder extends ByteToMessageDecoder
{
    private static final DatabaseInitializer databaseInitializer = DatabaseInitializer.getInstance();

    private final PacketDirection packetDirection;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        final Packet packet = databaseInitializer.getPacketRegistry()
                .newInstance(packetDirection, packetBuffer.readInt());

        packet.read(packetBuffer);
        list.add(packet);
    }
}