package me.hp888.database.initialize;

import me.hp888.database.DatabaseDriver;
import me.hp888.database.api.initialize.Initializer;
import me.hp888.database.api.packet.PacketDirection;
import me.hp888.database.communication.packets.client.ClientAuthenticationPacket;
import me.hp888.database.communication.packets.client.ClientInsertObjectPacket;
import me.hp888.database.communication.packets.client.ClientUpdateObjectPacket;

public final class PacketInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseDriver driver) {
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x00, ClientAuthenticationPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x01, ClientInsertObjectPacket.class);
        driver.getPacketRegistry().registerPacket(PacketDirection.TO_SERVER, 0x02, ClientUpdateObjectPacket.class);
    }
}