package me.hp888.database.api.config;

import com.google.gson.JsonObject;
import me.hp888.database.Bootstrap;
import me.hp888.database.DatabaseInitializer;
import me.hp888.database.utils.IOUtil;
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
            final InputStream inputStream = new FileInputStream("F:\\IdeaProjects\\SafeMC-Database\\DatabaseServer\\src\\main\\resources\\config.json");//Bootstrap.class.getResourceAsStream(fileName + ".json");
            Objects.requireNonNull(inputStream, "InputStream cannot be null!");

            if (!file.exists())
                IOUtil.write(inputStream, new FileOutputStream(file));

            configurations.put(fileName, databaseInitializer.getJsonParser().parse(new InputStreamReader(new FileInputStream(file)))
                    .getAsJsonObject()
            );
        }));
    }
}