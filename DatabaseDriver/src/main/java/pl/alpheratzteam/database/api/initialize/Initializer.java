package pl.alpheratzteam.database.api.initialize;

import pl.alpheratzteam.database.DatabaseDriver;

/**
 * @author hp888 on 19.04.2020.
 */

public interface Initializer
{
    void initialize(final DatabaseDriver driver);
}