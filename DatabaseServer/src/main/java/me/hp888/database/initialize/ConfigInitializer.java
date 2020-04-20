package me.hp888.database.initialize;

import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.initialize.Initializer;

/**
 * @author hp888 on 19.04.2020.
 */

public final class ConfigInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseInitializer databaseInitializer) {
        databaseInitializer.getConfigurationLoader()
                .checkConfigurationFiles("config");
    }
}