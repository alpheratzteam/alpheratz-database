package pl.alpheratzteam.database.api.database;

import io.netty.channel.ChannelFutureListener;
import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;
import io.netty.channel.Channel;
import pl.alpheratzteam.database.api.packet.Packet;
import java.util.Objects;
import lombok.Data;

/**
 * @author hp888 on 19.04.2020.
 */

@Data
public final class DatabaseClient
{
    private final DatabasePacketHandler packetHandler;
    private final Channel channel;

    public DatabaseClient(final Channel nettyChannel) {
        packetHandler = new DatabasePacketHandler(this);
        channel = nettyChannel;
    }

    private boolean authenticated;

    public void disconnect() {
        if (Objects.isNull(channel))
            return;

        channel.close();
        channel.flush();
    }

    public void sendPacket(final Packet packet) {
        if (Objects.isNull(channel) || !channel.isOpen())
            return;

        channel.writeAndFlush(packet)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}