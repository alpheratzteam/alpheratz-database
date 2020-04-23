import com.google.gson.JsonObject;
import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.database.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hp888 on 20.04.2020.
 */

public final class DatabaseTest
{
    public static void main(final String... args) {
        DatabaseDriver.INSTANCE.connect(new DatabaseClient(new DatabaseData("127.0.0.1", 2139, new DatabaseUser("root", "none"))), new ConnectCallback() {
            @Override
            public void success(DatabaseClient databaseClient) {
                System.out.println("Connected!");

                final Database database = DatabaseDriver.INSTANCE.getDatabaseRegistry().getDatabase(databaseClient, "alpheratz");
                final Collection collection = database.getCollection("test");
                collection.insert(insertObject());
                collection.update(new KeyData("nickname", "abc123"), updateObject());

                final Iterator<JsonObject> iterator = collection.find();
                final AtomicInteger index = new AtomicInteger();
                iterator.forEachRemaining(jsonObject -> System.out.println("Value at " + index.getAndIncrement() + " is: " + jsonObject.toString()));

                databaseClient.disconnect();
            }

            @Override
            public void error(Throwable cause) {
                System.err.println("Exception thrown!");
            }
        });
    }

    private static JsonObject insertObject() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", "abc123");
        return jsonObject;
    }

    private static JsonObject updateObject() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", "cba321");
        return jsonObject;
    }
}