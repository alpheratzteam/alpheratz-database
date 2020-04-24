package pl.alpheratzteam.database.api.database;

public interface ConnectCallback
{
    void success(final DatabaseClient databaseClient);
    
    void error(final Throwable cause);
}