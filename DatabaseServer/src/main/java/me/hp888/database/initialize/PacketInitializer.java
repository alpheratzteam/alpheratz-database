package me.hp888.database.initialize;

import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.initialize.Initializer;
import me.hp888.database.api.packet.PacketDirection;
import me.hp888.database.communication.packets.client.ClientAuthenticationPacket;
import me.hp888.database.communication.packets.client.ClientInsertObjectPacket;
import me.hp888.database.communication.packets.client.ClientUpdateObjectPacket;

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
    }
}