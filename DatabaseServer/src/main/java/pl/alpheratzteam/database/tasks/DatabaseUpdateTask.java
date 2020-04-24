package pl.alpheratzteam.database.tasks;

import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.database.Database;
import java.util.logging.Level;
import java.io.IOException;

/**
 * @author hp888 on 20.04.2020.
 */

@RequiredArgsConstructor
public final class DatabaseUpdateTask implements Runnable
{
    private final DatabaseInitializer databaseInitializer;

    @Override
    public void run() {
        databaseInitializer.getDatabaseRegistry().getDatabases()
                .stream()
                .filter(Database::isNeedUpdate)
                .forEach(database -> {
                    try {
                        database.save();
                    } catch (final IOException ex) {
                        databaseInitializer.getLogger().log(Level.SEVERE, "Exception thrown when saving database " + database.getName() + "...", ex);
                    }
                });

        databaseInitializer.getLogger()
                .info("Saved databases!");
    }
}