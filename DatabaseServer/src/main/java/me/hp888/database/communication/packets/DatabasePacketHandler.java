package me.hp888.database.communication.packets;

import lombok.RequiredArgsConstructor;
import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.database.DatabaseUser;
import me.hp888.database.communication.packets.client.ClientAuthenticationPacket;
import me.hp888.database.communication.packets.client.ClientInsertObjectPacket;
import me.hp888.database.communication.packets.client.ClientUpdateObjectPacket;
import me.hp888.database.objects.Collection;
import me.hp888.database.objects.Database;
import me.hp888.database.objects.DatabaseClient;
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