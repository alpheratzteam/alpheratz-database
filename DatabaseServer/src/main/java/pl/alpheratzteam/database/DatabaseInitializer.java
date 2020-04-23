package pl.alpheratzteam.database;

import com.google.gson.JsonParser;
import lombok.Getter;
import pl.alpheratzteam.database.api.config.ConfigurationLoader;
import pl.alpheratzteam.database.api.database.DatabaseServer;
import pl.alpheratzteam.database.api.execute.SafeExecutor;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.api.packet.PacketRegistry;
import pl.alpheratzteam.database.api.scheduler.Scheduler;
import pl.alpheratzteam.database.api.database.DatabaseRegistry;
import pl.alpheratzteam.database.scheduler.DatabaseScheduler;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

/**
 * @author hp888 on 18.04.2020.
 */

@Getter
public enum DatabaseInitializer
{
    INSTANCE;

    private final ConfigurationLoader configurationLoader;
    private final DatabaseRegistry databaseRegistry;
    private final PacketRegistry packetRegistry;
    private final DatabaseServer server;
    private final JsonParser jsonParser;
    private final Scheduler scheduler;
    private final File baseFolder;
    private final Logger logger;

    DatabaseInitializer() {
        (baseFolder = new File("Alpheratz-Database")).mkdirs();
        configurationLoader = new ConfigurationLoader(this);
        logger = Logger.getLogger("Alpheratz-Database");
        databaseRegistry = new DatabaseRegistry();
        packetRegistry = new PacketRegistry();
        scheduler = new DatabaseScheduler();
        jsonParser = new JsonParser();
        server = new DatabaseServer();
    }

    void load(final Set<Initializer> initializers) {
        initializers.forEach(initializer -> initializer.initialize(this));
    }

    public void safeExecute(final SafeExecutor safeExecutor) {
        try {
            safeExecutor.execute();
        } catch (final Throwable throwable) {
            logger.log(Level.SEVERE, "Exception thrown when executing some code...", throwable);
        }
    }
}