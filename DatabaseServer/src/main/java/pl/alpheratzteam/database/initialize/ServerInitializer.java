package pl.alpheratzteam.database.initialize;

import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.initialize.Initializer;

/**
 * @author hp888 on 19.04.2020.
 */

public final class ServerInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseInitializer databaseInitializer) {
        databaseInitializer.getServer()
                .listen();
    }
}