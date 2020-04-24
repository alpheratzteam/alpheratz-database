package pl.alpheratzteam.database.communication.packets.client;

import lombok.*;
import pl.alpheratzteam.database.api.packet.Packet;
import pl.alpheratzteam.database.api.packet.PacketBuffer;
import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;

/**
 * @author hp888 on 19.04.2020.
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public final class ClientAuthenticationPacket extends Packet
{
    private String username, password;

    @Override
    public void read(PacketBuffer packetBuffer) {
        username = packetBuffer.readStringFromBuffer(128);
        password = packetBuffer.readStringFromBuffer(512);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(username);
        packetBuffer.writeString(password);
    }

    @Override
    public void handlePacket(DatabasePacketHandler packetHandler) {
        packetHandler.handlePacket(this);
    }
}