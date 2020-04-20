package me.hp888.database.communication.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.hp888.database.DatabaseDriver;
import me.hp888.database.api.packet.Packet;
import me.hp888.database.api.packet.PacketBuffer;
import me.hp888.database.api.packet.PacketDirection;

/**
 * @author hp888 on 20.04.2020.
 */

@RequiredArgsConstructor
public final class PacketEncoder extends MessageToByteEncoder<Packet>
{
    private final PacketDirection packetDirection;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) {
        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        packetBuffer.writeInt(DatabaseDriver.INSTANCE.getPacketRegistry()
                .getPacketId(packetDirection, packet.getClass())
        );

        packet.write(packetBuffer);
    }
}