package pl.alpheratzteam.database.communication.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.api.packet.PacketBuffer;
import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;

/**
 * @author hp888 on 20.04.2020.
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public final class ClientRecordsRequestPacket extends CallbackPacket
{
    private String databaseName, collectionName;

    @Override
    public void read(PacketBuffer packetBuffer) {
        databaseName = packetBuffer.readStringFromBuffer(128);
        collectionName = packetBuffer.readStringFromBuffer(128);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(databaseName);
        packetBuffer.writeString(collectionName);
    }

    @Override
    public void handlePacket(DatabasePacketHandler packetHandler) {
        packetHandler.handlePacket(this);
    }
}