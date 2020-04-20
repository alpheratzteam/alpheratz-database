package pl.alpheratzteam.database.initialize;

import com.google.gson.JsonObject;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.database.DatabaseData;
import pl.alpheratzteam.database.api.database.DatabaseUser;
import pl.alpheratzteam.database.api.initialize.Initializer;

/**
 * @author hp888 on 19.04.2020.
 */

public final class DataInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseInitializer databaseInitializer) {
        final JsonObject config = databaseInitializer.getConfigurationLoader().getConfiguration("config"),
                authentication = config.get("authentication").getAsJsonObject(),
                data = authentication.get("data").getAsJsonObject();

        databaseInitializer.getServer().setData(new DatabaseData(config.get("host").getAsString(), config.get("port").getAsInt(),
                authentication.get("enabled").getAsBoolean(),
                new DatabaseUser(data.get("user").getAsString(), data.get("password").getAsString())
        ));
    }
}