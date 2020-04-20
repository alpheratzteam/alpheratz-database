package me.hp888.database.api.scheduler;

public interface ScheduledTask
{
    TaskStatus status();

    void schedule();

    void cancel();
}