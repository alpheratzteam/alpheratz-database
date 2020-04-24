package pl.alpheratzteam.database.api.database;

import lombok.Data;

/**
 * @author hp888 on 18.04.2020.
 */

@Data
public final class DatabaseData
{
    private final String host;
    private final int port;

    private final DatabaseUser databaseUser;
}