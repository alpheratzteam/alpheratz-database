package me.hp888.database.api.database;

import me.hp888.database.objects.DatabaseClient;

/**
 * @author hp888 on 20.04.2020.
 */

public interface ConnectCallback
{
    void success(final DatabaseClient databaseClient);
    
    void error(final Throwable cause);
}