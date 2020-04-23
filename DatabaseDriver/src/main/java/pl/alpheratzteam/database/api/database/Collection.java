package pl.alpheratzteam.database.api.database;

import lombok.Data;
import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.future.AsyncFuture;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientInsertObjectPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientRecordsRequestPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientUpdateObjectPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * @author hp888 on 20.04.2020.
 */

@Data
public final class Collection
{
    private final Map<Long, CompletableFuture<List<Document>>> findCache = new ConcurrentHashMap<>();

    private final Database database;
    private final String name;

    public void insert(final Document document) {
        database.getClient()
                .sendPacket(new ClientInsertObjectPacket(database.getName(), name, document.toString()));
    }

    public void update(final KeyData keyData, final Document document) {
        database.getClient()
                .sendPacket(new ClientUpdateObjectPacket(database.getName(), name, keyData.getKey(), keyData.getValue(), document.toString()));
    }

    public AsyncFuture<List<Document>> findAsync() {
        final AsyncFuture<List<Document>> future = new AsyncFuture<>();
        DatabaseDriver.INSTANCE.getScheduler().buildTask(() -> {
            try {
                final CallbackPacket requestPacket = new ClientRecordsRequestPacket(database.getName(), name);
                final CompletableFuture<List<Document>> completableFuture = findCache.computeIfAbsent(requestPacket.getCallbackId(), id -> new CompletableFuture<>());
                database.getClient()
                        .sendPacket(requestPacket);

                future.complete(completableFuture.get());
            } catch (final InterruptedException | ExecutionException ex) {
                DatabaseDriver.INSTANCE.getLogger().log(Level.SEVERE, "Can't read documents..", ex);
                future.complete(new ArrayList<>());
            }
        }).schedule();
        return future;
    }

    public List<Document> find() {
        try {
            final CallbackPacket requestPacket = new ClientRecordsRequestPacket(database.getName(), name);
            final CompletableFuture<List<Document>> completableFuture = findCache.computeIfAbsent(requestPacket.getCallbackId(), id -> new CompletableFuture<>());
            database.getClient()
                    .sendPacket(requestPacket);

            return completableFuture.get();
        } catch (final InterruptedException | ExecutionException ex) {
            DatabaseDriver.INSTANCE.getLogger().log(Level.SEVERE, "Can't read documents..", ex);
            return new ArrayList<>();
        }
    }
}