package pl.alpheratzteam.database.api.database;

import lombok.*;

/**
 * @author hp888 on 18.04.2020.
 */

@Data
public final class DatabaseUser
{
    private final String user, password;
}