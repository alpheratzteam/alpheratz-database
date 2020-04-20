package me.hp888.database.api.scheduler;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * @author hp888 on 03.07.2019.
 */

public enum TaskStatus
{
    RUNNING,
    CANCELLED,
    FINISHED;

    public static TaskStatus of(final ScheduledFuture<?> future) {
        if (Objects.isNull(future))
            return RUNNING;

        if (future.isCancelled())
            return CANCELLED;

        if (future.isDone())
            return FINISHED;

        return RUNNING;
    }
}