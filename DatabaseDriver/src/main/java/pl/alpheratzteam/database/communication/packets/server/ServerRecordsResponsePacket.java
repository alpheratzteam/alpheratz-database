package pl.alpheratzteam.database.communication.packets.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.api.packet.PacketBuffer;
import pl.alpheratzteam.database.communication.packets.DatabasePacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author hp888 on 21.04.2020.
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public final class ServerRecordsResponsePacket extends CallbackPacket
{
    private String databaseName, collectionName;
    private List<String> records = new ArrayList<>();

    @Override
    public void read(PacketBuffer packetBuffer) {
        databaseName = packetBuffer.readStringFromBuffer(128);
        collectionName = packetBuffer.readStringFromBuffer(128);

        final int size = packetBuffer.readInt();
        IntStream.range(0, size)
                .forEachOrdered(i -> records.add(packetBuffer.readStringFromBuffer(Short.MAX_VALUE)));
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(databaseName);
        packetBuffer.writeString(collectionName);
        packetBuffer.writeInt(records.size());
        records.forEach(packetBuffer::writeString);
    }

    @Override
    public void handlePacket(DatabasePacketHandler packetHandler) {
        packetHandler.handlePacket(this);
    }
}