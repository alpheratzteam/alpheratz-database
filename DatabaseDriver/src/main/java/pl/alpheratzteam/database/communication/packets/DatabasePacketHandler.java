package pl.alpheratzteam.database.communication.packets;

import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.database.Collection;
import pl.alpheratzteam.database.api.database.Database;
import pl.alpheratzteam.database.api.database.DatabaseClient;
import pl.alpheratzteam.database.communication.packets.server.ServerRecordsResponsePacket;

/**
 * @author hp888 on 19.04.2020.
 */

@RequiredArgsConstructor
public final class DatabasePacketHandler
{
    private final DatabaseClient client;

    public void handlePacket(final ServerRecordsResponsePacket packet) {
        final Database database = DatabaseDriver.INSTANCE.getDatabaseRegistry().getDatabase(client, packet.getDatabaseName());
        final Collection collection = database.getCollection(packet.getCollectionName());
        if (!collection.getFindCache().containsKey(packet.getCallbackId()))
            return;

        collection.getFindCache().get(packet.getCallbackId()).complete(packet.getRecords().stream()
                .map(record -> DatabaseDriver.INSTANCE.getJsonParser().parse(record).getAsJsonObject())
                .iterator()
        );
    }
}