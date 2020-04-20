package me.hp888.database.scheduler;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.hp888.database.api.scheduler.ScheduledTask;
import me.hp888.database.api.scheduler.Scheduler;
import me.hp888.database.api.scheduler.TaskStatus;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.*;
import java.util.*;

public final class DatabaseScheduler implements Scheduler
{
    private final ExecutorService taskService;
    private final ScheduledExecutorService timerExecutionService;
    private final Multimap<Integer, ScheduledTask> tasksById;

    public DatabaseScheduler() {
        taskService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true)
                .setNameFormat("Database Task Scheduler - #%d").build());
        timerExecutionService = Executors
                .newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true)
                        .setNameFormat("Database Task Scheduler Timer").build());
        tasksById = Multimaps.synchronizedMultimap(
                Multimaps.newSetMultimap(new IdentityHashMap<>(), HashSet::new));
    }

    @Override
    public Collection<ScheduledTask> getTasks() {
        return Collections.unmodifiableCollection(tasksById.values());
    }

    @Override
    public TaskBuilder buildTask(Runnable runnable) {
        return new TaskBuilderImpl(runnable);
    }

    private class TaskBuilderImpl implements TaskBuilder
    {
        private final Runnable runnable;
        private long delay; // ms
        private long repeat; // ms

        private TaskBuilderImpl(final Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public TaskBuilder delay(long time, TimeUnit unit) {
            delay = unit.toMillis(time);
            return this;
        }

        @Override
        public TaskBuilder repeat(long time, TimeUnit unit) {
            repeat = unit.toMillis(time);
            return this;
        }

        @Override
        public TaskBuilder clearDelay() {
            delay = 0;
            return this;
        }

        @Override
        public TaskBuilder clearRepeat() {
            repeat = 0;
            return this;
        }

        @Override
        public ScheduledTask schedule() {
            final int id = tasksById.size() + 1;
            final ScheduledTask task = new ProxyTask(id, runnable, delay, repeat);
            tasksById.put(id, task);
            task.schedule();
            return task;
        }
    }

    private class ProxyTask implements Runnable, ScheduledTask
    {
        private final int id;
        private final Runnable runnable;
        private final long delay;
        private final long repeat;
        private ScheduledFuture<?> future;
        private volatile Thread currentTaskThread;

        private ProxyTask(final int id, final Runnable runnable, final long delay, final long repeat) {
            this.id = id;
            this.runnable = runnable;
            this.delay = delay;
            this.repeat = repeat;
        }

        @Override
        public void schedule() {
            if (repeat == 0) {
                future = timerExecutionService.schedule(this, delay, TimeUnit.MILLISECONDS);
            } else {
                future = timerExecutionService
                        .scheduleAtFixedRate(this, delay, repeat, TimeUnit.MILLISECONDS);
            }
        }

        @Override
        public TaskStatus status() {
            return TaskStatus.of(future);
        }

        @Override
        public void cancel() {
            if (Objects.isNull(future))
                return;

            future.cancel(false);

            if (Objects.nonNull(currentTaskThread))
                currentTaskThread.interrupt();

            this.onFinish();
        }

        @Override
        public void run() {
            taskService.execute(() -> {
                currentTaskThread = Thread.currentThread();
                try {
                    runnable.run();
                } catch (final Exception ex) {
                    Logger.getLogger("Database").log(Level.SEVERE, "Exception in task " + runnable + " by id " + id, ex);
                } finally {
                    currentTaskThread = null;
                }
            });
        }

        private void onFinish() {
            tasksById.remove(id, this);
        }
    }
}