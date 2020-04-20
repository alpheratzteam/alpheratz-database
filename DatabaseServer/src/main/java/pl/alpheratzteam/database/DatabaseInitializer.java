package pl.alpheratzteam.database;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonParser;
import lombok.Getter;
import pl.alpheratzteam.database.api.config.ConfigurationLoader;
import pl.alpheratzteam.database.api.execute.SafeExecutor;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.api.packet.PacketRegistry;
import pl.alpheratzteam.database.api.scheduler.Scheduler;
import pl.alpheratzteam.database.managers.DatabaseManager;
import pl.alpheratzteam.database.objects.DatabaseServer;
import pl.alpheratzteam.database.scheduler.DatabaseScheduler;
import pl.alpheratzteam.database.initialize.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

/**
 * @author hp888 on 18.04.2020.
 */

@Getter
public final class DatabaseInitializer
{
    @Getter
    private static DatabaseInitializer instance;

    private final ImmutableSet<Initializer> initializers;

    private final ConfigurationLoader configurationLoader;
    private final DatabaseManager databaseManager;
    private final PacketRegistry packetRegistry;
    private final DatabaseServer server;
    private final JsonParser jsonParser;
    private final Scheduler scheduler;
    private final File baseFolder;
    private final Logger logger;

    DatabaseInitializer() {
        instance = this;

        server = new DatabaseServer();
        jsonParser = new JsonParser();
        scheduler = new DatabaseScheduler();
        packetRegistry = new PacketRegistry();
        logger = Logger.getLogger("Alpheratz-Database");
        databaseManager = new DatabaseManager();
        (baseFolder = new File("Alpheratz-Database")).mkdirs();
        configurationLoader = new ConfigurationLoader(this);

        initializers = ImmutableSet.<Initializer>builder()
                .add(new ConfigInitializer(), new DataInitializer(), new PacketInitializer(), new ServerInitializer(), new SchedulerInitializer())
                .build();
    }

    void load() {
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