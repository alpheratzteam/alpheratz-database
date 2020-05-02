package pl.alpheratzteam.database.communication.packets.server;

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
public final class ServerRecordResponsePacket extends CallbackPacket
{
    private String databaseName, collectionName, record;

    @Override
    public void read(PacketBuffer packetBuffer) {
        databaseName = packetBuffer.readStringFromBuffer(128);
        collectionName = packetBuffer.readStringFromBuffer(128);
        record = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(databaseName);
        packetBuffer.writeString(collectionName);
        packetBuffer.writeString(record);
    }

    @Override
    public void handlePacket(DatabasePacketHandler packetHandler) {
        packetHandler.handlePacket(this);
    }
}