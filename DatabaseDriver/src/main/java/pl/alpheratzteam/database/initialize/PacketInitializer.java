package pl.alpheratzteam.database.initialize;

import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.api.packet.PacketDirection;
import pl.alpheratzteam.database.communication.packets.client.*;
import pl.alpheratzteam.database.communication.packets.server.ServerRecordResponsePacket;
import pl.alpheratzteam.database.communication.packets.server.ServerRecordsResponsePacket;

public final class PacketInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseDriver driver) {
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x00, ClientAuthenticationPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x01, ClientInsertObjectPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x02, ClientUpdateObjectPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x03, ClientRecordsRequestPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x04, ClientRecordRequestPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x05, ClientInsertOrUpdateObjectPacket.class);

        driver.getPacketRegistry().registerPacket(PacketDirection.TO_CLIENT, 0x00, ServerRecordsResponsePacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_CLIENT, 0x01, ServerRecordResponsePacket.class);
    }
}