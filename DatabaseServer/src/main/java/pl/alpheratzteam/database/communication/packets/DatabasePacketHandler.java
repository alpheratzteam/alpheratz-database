package pl.alpheratzteam.database.communication.packets;

import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.database.DatabaseUser;
import pl.alpheratzteam.database.communication.packets.client.ClientAuthenticationPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientInsertObjectPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientUpdateObjectPacket;
import pl.alpheratzteam.database.objects.Collection;
import pl.alpheratzteam.database.objects.Database;
import pl.alpheratzteam.database.objects.DatabaseClient;

import java.net.InetSocketAddress;

/**
 * @author hp888 on 19.04.2020.
 */

@RequiredArgsConstructor
public final class DatabasePacketHandler
{
    private final DatabaseClient client;

    public void handlePacket(final ClientAuthenticationPacket packet) {
        final DatabaseInitializer database = DatabaseInitializer.getInstance();
        if (!database.getServer().getData().isAuthenticationEnabled())
            return;

        final DatabaseUser databaseUser = database.getServer()
                .getData().getDatabaseUser();

        client.setAuthenticated(packet.getUsername().equals(databaseUser.getUser()) && packet.getPassword().equals(databaseUser.getPassword()));
        client.disconnect();

        database.getLogger().warning("Incorrect authentication data from " + ((InetSocketAddress) client.getChannel().remoteAddress()).getAddress().getHostAddress());
    }

    public void handlePacket(final ClientUpdateObjectPacket packet) {
       /*final Database database = DatabaseInitializer.getInstance().getDatabaseManager()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        collection.getRecords().add(packet.getJson());

*/

        System.out.println("Received packet: " + packet.toString());
    }

    public void handlePacket(final ClientInsertObjectPacket packet) {
        final Database database = DatabaseInitializer.getInstance().getDatabaseManager()
                .getDatabase(packet.getDatabaseName());

        final Collection collection = database.getCollection(packet.getCollectionName());
        collection.getRecords().add(packet.getJsonObject());
        database.setNeedUpdate(true);

        System.out.println("Added record: " + packet.getJsonObject());
    }
}