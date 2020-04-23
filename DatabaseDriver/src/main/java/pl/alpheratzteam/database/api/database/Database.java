package pl.alpheratzteam.database.api.database;

import lombok.Data;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hp888 on 20.04.2020.
 */

@Data
public final class Database
{
    private final Map<String, Collection> collections = new ConcurrentHashMap<>();
    private final DatabaseClient client;
    private final String name;

    public Collection getCollection(final String name) {
        return collections.computeIfAbsent(name, str -> new Collection(this, name));
    }

    public void removeCollection(final String name) {
        collections.remove(name);
    }
}