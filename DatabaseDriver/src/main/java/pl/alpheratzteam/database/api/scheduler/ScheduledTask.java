package pl.alpheratzteam.database.api.scheduler;

public interface ScheduledTask
{
    TaskStatus status();

    void schedule();

    void cancel();
}