package pl.alpheratzteam.database.objects;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import pl.alpheratzteam.database.api.database.DatabaseData;
import pl.alpheratzteam.database.api.packet.Packet;
import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;

import java.util.Objects;

/**
 * @author hp888 on 19.04.2020.
 */

@Data
public final class DatabaseClient
{
    private final DatabaseData data;
    private final DatabasePacketHandler packetHandler;

    public DatabaseClient(final DatabaseData databaseData) {
        data = databaseData;
        packetHandler = new DatabasePacketHandler(this);
    }

    private Channel channel;

    public void sendPacket(final Packet packet) {
        if (Objects.isNull(channel))
            return;

        channel.writeAndFlush(packet)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}