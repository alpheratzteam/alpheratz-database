package pl.alpheratzteam.database.api.database;

import com.google.gson.JsonObject;
import lombok.Data;
import pl.alpheratzteam.database.api.packet.CallbackPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientInsertObjectPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientRecordsRequestPacket;
import pl.alpheratzteam.database.communication.packets.client.ClientUpdateObjectPacket;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * @author hp888 on 20.04.2020.
 */

@Data
public final class Collection
{
    private final Map<Long, CompletableFuture<Iterator<JsonObject>>> findCache = new ConcurrentHashMap<>();
    private final Database database;
    private final String name;

    public void insert(final JsonObject jsonObject) {
        database.getClient().sendPacket(new ClientInsertObjectPacket(database.getName(), name, jsonObject.toString()));
    }

    public void update(final KeyData keyData, final JsonObject jsonObject) {
        database.getClient().sendPacket(new ClientUpdateObjectPacket(database.getName(), name, keyData.getKey(), keyData.getValue(), jsonObject.toString()));
    }

    public Iterator<JsonObject> find() {
        try {
            final CallbackPacket requestPacket = new ClientRecordsRequestPacket(database.getName(), name);
            final CompletableFuture<Iterator<JsonObject>> completableFuture = findCache.computeIfAbsent(requestPacket.getCallbackId(), id -> new CompletableFuture<>());
            database.getClient()
                    .sendPacket(requestPacket);

            return completableFuture.get();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}