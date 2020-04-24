package pl.alpheratzteam.database.communication.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.api.packet.PacketBuffer;
import pl.alpheratzteam.database.api.packet.PacketDirection;

/**
 * @author hp888 on 20.04.2020.
 */

@RequiredArgsConstructor
public final class PacketEncoder extends MessageToByteEncoder<CallbackPacket>
{
    private final PacketDirection packetDirection;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CallbackPacket packet, ByteBuf byteBuf) {
        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        final int packetId = DatabaseInitializer.INSTANCE.getPacketRegistry()
                .getPacketId(packetDirection, packet.getClass());

        if (packetId == -1)
            throw new EncoderException("Cannot encode unregistered packet! (" + packet.getClass().getSimpleName() + ")");

        packetBuffer.writeVarIntToBuffer(packetId);
        packet.write(packetBuffer);
    }
}