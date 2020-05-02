package pl.alpheratzteam.database.api.config;

import com.google.gson.JsonObject;
import pl.alpheratzteam.database.Bootstrap;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.utils.IOUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Objects;

/**
 * @author hp888 on 18.04.2020.
 */

public final class ConfigurationLoader
{
    private final Map<String, JsonObject> configurations;
    private final DatabaseInitializer databaseInitializer;

    public ConfigurationLoader(final DatabaseInitializer initializer) {
        configurations = new HashMap<>();
        databaseInitializer = initializer;
    }

    public JsonObject getConfiguration(final String name) {
        return configurations.get(name);
    }

    public void checkConfigurationFiles(final String... files) {
        Arrays.stream(files).forEach(fileName -> databaseInitializer.safeExecute(() -> {
            final File file = new File(databaseInitializer.getBaseFolder(), fileName + ".json");
            final InputStream inputStream = Bootstrap.class.getResourceAsStream("/" + fileName + ".json");
            Objects.requireNonNull(inputStream, "InputStream cannot be null!");

            if (!file.exists())
                IOUtil.write(inputStream, new FileOutputStream(file));

            configurations.put(fileName, databaseInitializer.getJsonParser().parse(new InputStreamReader(new FileInputStream(file)))
                    .getAsJsonObject()
            );
        }));
    }
}