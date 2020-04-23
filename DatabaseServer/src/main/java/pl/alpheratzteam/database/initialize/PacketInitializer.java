package pl.alpheratzteam.database.initialize;

import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.api.packet.PacketDirection;
import pl.alpheratzteam.database.communication.packets.client.ClientAuthenticationPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientInsertObjectPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientRecordsRequestPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientUpdateObjectPacket;
import pl.alpheratzteam.database.communication.packets.server.ServerRecordsResponsePacket;

/**
 * @author hp888 on 19.04.2020.
 */

public final class PacketInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseInitializer databaseInitializer) {
        databaseInitializer.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x00, ClientAuthenticationPacket.class);
        databaseInitializer.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x01, ClientInsertObjectPacket.class);
        databaseInitializer.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x02, ClientUpdateObjectPacket.class);
        databaseInitializer.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x03, ClientRecordsRequestPacket.class);

        databaseInitializer.getPacketRegistry().registerPacket(PacketDirection.TO_CLIENT, 0x00, ServerRecordsResponsePacket.class);
    }
}