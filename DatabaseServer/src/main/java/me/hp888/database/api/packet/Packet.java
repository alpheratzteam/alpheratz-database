package me.hp888.database.api.packet;

import me.hp888.database.communication.packets.DatabasePacketHandler;

/**
 * @author hp888 on 19.04.2020.
 */

public abstract class Packet
{
    public abstract void read(final PacketBuffer packetBuffer);

    public abstract void write(final PacketBuffer packetBuffer);

    public void handlePacket(final DatabasePacketHandler packetHandler) {}
}