package pl.alpheratzteam.database.initialize;

import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.tasks.DatabaseUpdateTask;

import java.util.concurrent.TimeUnit;

/**
 * @author hp888 on 20.04.2020.
 */

public final class SchedulerInitializer implements Initializer
{
    @Override
    public void initialize(DatabaseInitializer databaseInitializer) {
        final Runnable databaseUpdateTask = new DatabaseUpdateTask(databaseInitializer);
        databaseInitializer.getScheduler().buildTask(databaseUpdateTask)
                .delay(1L, TimeUnit.MINUTES)
                .repeat(1L, TimeUnit.MINUTES)
                .schedule();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(databaseUpdateTask));
    }
}