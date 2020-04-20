package me.hp888.database.api.execute;

/**
 * @author hp888 on 18.04.2020.
 */

public interface SafeExecutor
{
    void execute() throws Throwable;
}