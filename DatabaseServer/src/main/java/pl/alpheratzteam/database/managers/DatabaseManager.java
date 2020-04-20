package pl.alpheratzteam.database.managers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.database.DatabaseReader;
import pl.alpheratzteam.database.objects.Database;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author hp888 on 20.04.2020.
 */

public final class DatabaseManager
{
    private final Map<String, Database> databases;

    public DatabaseManager() {
        databases = new ConcurrentHashMap<>();
        loadAll();
    }

    public Collection<Database> getDatabases() {
        return databases.values();
    }

    public Database getDatabase(final String name) {
        return databases.computeIfAbsent(name, str -> new Database(name, new ConcurrentHashMap<>()));
    }

    public void deleteDatabase(final String name) {
        final Database database = databases.remove(name);
        if (Objects.isNull(database))
            return;

        final File file = new File("databases", database.getName() + ".adb");
        if (!file.exists())
            return;

        file.delete();
    }

    private void loadAll() {
        final File databasesFolder = new File("databases");
        if (!databasesFolder.exists() || Objects.isNull(databasesFolder.listFiles()))
            return;

        Arrays.stream(databasesFolder.listFiles())
                .forEach(file -> {
                    try {
                        final Database database = new Database(file.getName().replace(".adb", ""));
                        DatabaseReader.INSTANCE.read(database, file);
                        databases.put(database.getName(), database);
                    } catch (final IOException ex) {
                        DatabaseInitializer.getInstance().getLogger().log(Level.SEVERE, "Exception thrown when loading databases...", ex);
                    }
                });

        DatabaseInitializer.getInstance().getLogger().info("Loaded " + databases.size() + " databases.");
    }
}