package pl.alpheratzteam.database;

import pl.alpheratzteam.database.api.database.ConnectCallback;
import pl.alpheratzteam.database.api.database.DatabaseData;
import pl.alpheratzteam.database.api.database.DatabaseUser;
import pl.alpheratzteam.database.communication.packets.client.ClientInsertObjectPacket;
import pl.alpheratzteam.database.objects.DatabaseClient;

/**
 * @author hp888 on 20.04.2020.
 */

public final class DatabaseTest
{
    public static void main(final String... args) {
        DatabaseDriver.INSTANCE.connect(new DatabaseClient(new DatabaseData("127.0.0.1", 2139, new DatabaseUser("root", "none"))), new ConnectCallback() {
            @Override
            public void success(DatabaseClient databaseClient) {
                databaseClient.sendPacket(new ClientInsertObjectPacket("baza", "kolekcja", "{\"nickname\":\"test\"}"));
            }

            @Override
            public void error(Throwable cause) {
                System.err.println("Exception thrown!");
            }
        });
    }
}