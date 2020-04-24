package pl.alpheratzteam.database.api.packet;

import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;

/**
 * @author hp888 on 19.04.2020.
 */

public abstract class Packet
{
    public abstract void read(final PacketBuffer packetBuffer);

    public abstract void write(final PacketBuffer packetBuffer);

    public void handlePacket(final DatabasePacketHandler packetHandler) {}
}