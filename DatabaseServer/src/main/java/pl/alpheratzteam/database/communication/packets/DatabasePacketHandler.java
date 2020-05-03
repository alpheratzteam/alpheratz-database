package pl.alpheratzteam.database.communication.packets;

import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.database.*;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.api.search.SearchResponse;
import pl.alpheratzteam.database.communication.packets.client.*;
import pl.alpheratzteam.database.communication.packets.server.ServerRecordResponsePacket;
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

    public void handlePacket(final ClientInsertOrUpdateObjectPacket packet) {
        final Database database = DatabaseInitializer.INSTANCE.getDatabaseRegistry()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        final int index = SearchUtil.getPosition(collection, new KeyData(packet.getKey(), packet.getValue()));
        if (index < 0) {
            collection.getRecords().add(packet.getJsonObject());
            database.setNeedUpdate(true);
          //  DatabaseInitializer.INSTANCE.getLogger().warning("not found object by key: " + packet.getKey() + " -> " + packet.getValue());
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

        System.out.println("Inserted: " + packet.getJsonObject() + ", " + collection.getRecords().toString());
    }

    public void handlePacket(final ClientRecordsRequestPacket packet) {
        final Database database = DatabaseInitializer.INSTANCE.getDatabaseRegistry()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        final CallbackPacket callbackPacket = new ServerRecordsResponsePacket(database.getName(), collection.getName(), collection.getRecords());
        callbackPacket.setCallbackId(packet.getCallbackId());
        client.sendPacket(callbackPacket);
    }

    public void handlePacket(final ClientRecordRequestPacket packet) {
        final Database database = DatabaseInitializer.INSTANCE.getDatabaseRegistry()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        final SearchResponse searchResponse = SearchUtil.search(collection, new KeyData(packet.getKey(), packet.getValue()));
        if (searchResponse.getIndex() < 0) {
            DatabaseInitializer.INSTANCE.getLogger().warning("not found object by key: " + packet.getKey() + " -> " + packet.getValue());
            client.sendPacket(new ServerRecordResponsePacket(packet.getDatabaseName(), packet.getCollectionName(), "{\"null_value\":true}"));
            return;
        }

        client.sendPacket(new ServerRecordResponsePacket(packet.getDatabaseName(), packet.getCollectionName(), searchResponse.getValue()));
    }
}