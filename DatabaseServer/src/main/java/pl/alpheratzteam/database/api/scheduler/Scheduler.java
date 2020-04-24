package pl.alpheratzteam.database.api.scheduler;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface Scheduler
{
    Collection<ScheduledTask> getTasks();

    TaskBuilder buildTask(final Runnable runnable);

    interface TaskBuilder
    {
        TaskBuilder delay(final long time, final TimeUnit unit);

        TaskBuilder repeat(final long time, final TimeUnit unit);

        TaskBuilder clearDelay();

        TaskBuilder clearRepeat();

        ScheduledTask schedule();
    }
}