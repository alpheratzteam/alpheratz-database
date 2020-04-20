package me.hp888.database.api.initialize;

import me.hp888.database.DatabaseInitializer;

/**
 * @author hp888 on 19.04.2020.
 */

public interface Initializer
{
    void initialize(final DatabaseInitializer databaseInitializer);
}