package me.hp888.database.communication.packets.client;

import lombok.*;
import me.hp888.database.api.packet.Packet;
import me.hp888.database.api.packet.PacketBuffer;
import me.hp888.database.communication.packets.DatabasePacketHandler;

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