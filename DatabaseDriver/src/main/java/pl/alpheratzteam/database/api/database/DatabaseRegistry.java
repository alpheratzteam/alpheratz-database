package pl.alpheratzteam.database.api.database;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Unix
 * @since 21.04.20
 */
public class DatabaseRegistry {
    private final Map<String, Database> databases;

    public DatabaseRegistry() {
        databases = new ConcurrentHashMap<>();
    }

    public Collection<Database> getDatabases() {
        return databases.values();
    }

    public Database getDatabase(final DatabaseClient client, final String name) {
        return databases.computeIfAbsent(name, str -> new Database(client, name));
    }

    public void deleteDatabase(final String name) {
        databases.remove(name);
    }
}