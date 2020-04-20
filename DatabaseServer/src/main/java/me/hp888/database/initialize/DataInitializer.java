package me.hp888.database.initialize;

import com.google.gson.JsonObject;
import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.database.DatabaseData;
import me.hp888.database.api.database.DatabaseUser;
import me.hp888.database.api.initialize.Initializer;

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