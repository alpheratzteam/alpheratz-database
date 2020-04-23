package pl.alpheratzteam.database.communication.packets;

import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.database.*;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientAuthenticationPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientInsertObjectPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientRecordsRequestPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientUpdateObjectPacket;
import pl.alpheratzteam.database.communication.packets.server.ServerRecordsResponsePacket;
import pl.alpheratzteam.database.utils.SearchUtil;
import java.net.InetSocketAddress;

/**
 * @author hp888 on 19.04.2020.
 */

@RequiredArgsConstructor
public final class DatabasePacketHandler
{
    private final DatabaseClient client;

    public void handlePacket(final ClientAuthenticationPacket packet) {
        final DatabaseInitializer database = DatabaseInitializer.INSTANCE;
        if (!database.getServer().getData().isAuthenticationEnabled()) {
            client.setAuthenticated(true);
            return;
        }

        final DatabaseUser databaseUser = database.getServer()
                .getData().getDatabaseUser();

        client.setAuthenticated(packet.getUsername().equals(databaseUser.getUser()) && packet.getPassword().equals(databaseUser.getPassword()));
        client.disconnect();

        database.getLogger().warning("Incorrect authentication data from " + ((InetSocketAddress) client.getChannel().remoteAddress()).getAddress().getHostAddress());
    }

    public void handlePacket(final ClientUpdateObjectPacket packet) {
        final Database database = DatabaseInitializer.INSTANCE.getDatabaseRegistry()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        final int index = SearchUtil.getPosition(collection, new KeyData(packet.getKey(), packet.getValue()));
        if (index < 0) {
            DatabaseInitializer.INSTANCE.getLogger().warning("not found object by key: " + packet.getKey() + " -> " + packet.getValue());
            return;
        }

        collection.getRecords().remove(index);
        collection.getRecords().add(packet.getJsonObject());
        database.setNeedUpdate(true);
    }

    public void handlePacket(final ClientInsertObjectPacket packet) {
        final Database database = DatabaseInitializer.INSTANCE.getDatabaseRegistry()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        collection.getRecords().add(packet.getJsonObject());
        database.setNeedUpdate(true);
    }

    public void handlePacket(final ClientRecordsRequestPacket packet) {
        final Database database = DatabaseInitializer.INSTANCE.getDatabaseRegistry()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        final CallbackPacket callbackPacket = new ServerRecordsResponsePacket(database.getName(), collection.getName(), collection.getRecords());
        callbackPacket.setCallbackId(packet.getCallbackId());
        client.sendPacket(callbackPacket);
    }
}