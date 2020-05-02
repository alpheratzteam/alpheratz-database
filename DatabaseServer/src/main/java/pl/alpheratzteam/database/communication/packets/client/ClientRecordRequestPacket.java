package pl.alpheratzteam.database.communication.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.api.packet.PacketBuffer;
import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;

/**
 * @author hp888 on 26.04.2020.
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public final class ClientRecordRequestPacket extends CallbackPacket
{
    private String databaseName, collectionName, key, value;

    public void read(PacketBuffer packetBuffer) {
        databaseName = packetBuffer.readStringFromBuffer(128);
        collectionName = packetBuffer.readStringFromBuffer(128);
        key = packetBuffer.readStringFromBuffer(128);
        value = packetBuffer.readStringFromBuffer(128);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(databaseName);
        packetBuffer.writeString(collectionName);
        packetBuffer.writeString(key);
        packetBuffer.writeString(value);
    }

    @Override
    public void handlePacket(DatabasePacketHandler packetHandler) {
        packetHandler.handlePacket(this);
    }
}