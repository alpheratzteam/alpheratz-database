package pl.alpheratzteam.database.objects;

import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
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

    public DatabaseClient() {
        packetHandler = new DatabasePacketHandler(this);
    }

    private boolean authenticated;
    private Channel channel;

    public void disconnect() {
        if (Objects.isNull(channel))
            return;

        channel.close();
        channel.flush();
    }

    public void sendPacket(final Packet packet) {
        if (Objects.isNull(channel))
            return;

        channel.writeAndFlush(packet)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}