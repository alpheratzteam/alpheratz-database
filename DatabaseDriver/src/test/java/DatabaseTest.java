import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.database.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author hp888 on 20.04.2020.
 */

public final class DatabaseTest
{
    public static void main(final String... args) {
        DatabaseDriver.INSTANCE.connect(new DatabaseClient(new DatabaseData("localhost", 2139, new DatabaseUser("root", "magia123"))), new ConnectCallback() {
            @Override
            public void success(DatabaseClient databaseClient) {
                System.out.println("Connected!");

                final Database database = DatabaseDriver.INSTANCE.getDatabaseRegistry().getDatabase(databaseClient, "alpheratz");
                final Collection collection = database.getCollection("test2");
               //IntStream.range(0, 10000).forEachOrdered(i -> collection.insert(insertObject()));
//                collection.insert(insertObject());
//                collection.update(new KeyData("nickname", "abc123"), updateObject());

               // final List<Document> documents = collection.find();
              //  final AtomicInteger index = new AtomicInteger();
               // documents.forEach(document -> System.out.println("Value at " + index.getAndIncrement() + " is: " + document.getString("nickname")));

               // collection.insertOrUpdate(new KeyData("nickname", "abc123"), updateObject());

                final long startTime = System.currentTimeMillis();
                collection.findAsync().setFutureListener((documents) -> {
                    DatabaseDriver.INSTANCE.getLogger().info("Found " + documents.size() + " documents in " + (System.currentTimeMillis() - startTime) + " ms.");

                    final AtomicInteger index = new AtomicInteger();
                    documents.forEach(document -> System.out.println("Value at " + index.getAndIncrement() + " is: " + document.getString("nickname")));
                });

                collection.findAsync(new KeyData("nickname", "cba321")).setFutureListener((document) -> {
                    System.out.println("found: " + (Objects.nonNull(document) ? document.toString() : "null"));
                });


               // databaseClient.disconnect();
            }

            @Override
            public void error(Throwable cause) {
                System.err.println("Exception thrown!");
            }
        });
    }

    private static Document insertObject() {
        return Document.parse("{\"nickname\":\"abc123\"}");
    }

    private static Document updateObject() {
        return Document.parse("{\"nickname\":\"cba321\"}");
    }
}