package pl.alpheratzteam.database;

/**
 * @author hp888 on 18.04.2020.
 */

public final class Bootstrap
{
    public static void main(final String... args) {
        new DatabaseInitializer().load();
    }
}