package me.hp888.database;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.hp888.database.api.config.ConfigurationLoader;
import me.hp888.database.api.execute.SafeExecutor;
import me.hp888.database.api.initialize.Initializer;
import me.hp888.database.api.packet.PacketRegistry;
import me.hp888.database.api.scheduler.Scheduler;
import me.hp888.database.initialize.*;
import me.hp888.database.managers.DatabaseManager;
import me.hp888.database.objects.DatabaseServer;
import me.hp888.database.scheduler.DatabaseScheduler;
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
        logger = Logger.getLogger("Database");
        databaseManager = new DatabaseManager();
        (baseFolder = new File("SafeMC-Database")).mkdirs();
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