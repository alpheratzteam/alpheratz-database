package me.hp888.database.communication.packets.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.hp888.database.api.packet.Packet;
import me.hp888.database.api.packet.PacketBuffer;

/**
 * @author hp888 on 20.04.2020.
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public final class ClientInsertObjectPacket extends Packet
{
    private String databaseName, collectionName, jsonObject;

    @Override
    public void read(PacketBuffer packetBuffer) {
        databaseName = packetBuffer.readStringFromBuffer(128);
        collectionName = packetBuffer.readStringFromBuffer(128);
        jsonObject = packetBuffer.readStringFromBuffer(Integer.MAX_VALUE);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(databaseName);
        packetBuffer.writeString(collectionName);
        packetBuffer.writeString(jsonObject);
    }
}