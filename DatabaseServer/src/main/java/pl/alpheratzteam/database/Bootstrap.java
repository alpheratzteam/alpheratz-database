package pl.alpheratzteam.database;

import com.google.common.collect.ImmutableSet;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.initialize.*;

/**
 * @author hp888 on 18.04.2020.
 */

public final class Bootstrap
{
    public static void main(final String... args) {
        DatabaseInitializer.INSTANCE.load(ImmutableSet.<Initializer>builder()
                .add(new ConfigInitializer(), new DataInitializer(), new PacketInitializer(), new ServerInitializer(), new SchedulerInitializer())
                .build()
        );
    }
}