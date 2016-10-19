package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.Utils;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nukkit Project Team
 */
public class ServerScheduler {

    public static int WORKERS = 4;

    private final AsyncPool asyncPool;

    private final Queue<TaskHandler> pending;
//    private final Queue<TaskHandler> queue;
    private final Map<Integer, ArrayDeque<TaskHandler>> queueMap;
    private final Map<Integer, TaskHandler> taskMap;
    private final AtomicInteger currentTaskId;

    private volatile int currentTick;

    public ServerScheduler() {
        this.pending = new ConcurrentLinkedQueue<>();
        this.currentTaskId = new AtomicInteger();
        this.queueMap = new ConcurrentHashMap<>();
//        this.queue = new PriorityQueue<>(11, (left, right) -> {
//            int i = left.getNextRunTick() - right.getNextRunTick();
//            if (i == 0) {
//                return left.getTaskId() - right.getTaskId();
//            }
//            return i;
//        });
        this.taskMap = new ConcurrentHashMap<>(8, 0.9f, 1);
        this.asyncPool = new AsyncPool(Server.getInstance(), WORKERS);
    }

    public TaskHandler scheduleTask(Task task) {
        return addTask(task, 0, 0, false);
    }

    public TaskHandler scheduleTask(Runnable task) {
        return addTask(null, task, 0, 0, false);
    }

    public TaskHandler scheduleTask(Runnable task, boolean asynchronous) {
        return addTask(null, task, 0, 0, asynchronous);
    }

    public TaskHandler scheduleAsyncTask(AsyncTask task) {
        return addTask(null, task, 0, 0, true);
    }

    @Deprecated
    public void scheduleAsyncTaskToWorker(AsyncTask task, int worker) {
        scheduleAsyncTask(task);
    }

    public int getAsyncTaskPoolSize() {
        return asyncPool.getSize();
    }

    public void increaseAsyncTaskPoolSize(int newSize) {
        throw new UnsupportedOperationException("Cannot increase a working pool size.");
    }

    public TaskHandler scheduleDelayedTask(Task task, int delay) {
        return this.addTask(task, delay, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Task task, int delay, boolean asynchronous) {
        return this.addTask(task, delay, 0, asynchronous);
    }

    public TaskHandler scheduleDelayedTask(Runnable task, int delay) {
        return addTask(null, task, delay, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Runnable task, int delay, boolean asynchronous) {
        return addTask(null, task, delay, 0, asynchronous);
    }

    public TaskHandler scheduleRepeatingTask(Runnable task, int period) {
        return addTask(null, task, 0, period, false);
    }

    public TaskHandler scheduleRepeatingTask(Runnable task, int period, boolean asynchronous) {
        return addTask(null, task, 0, period, asynchronous);
    }

    public TaskHandler scheduleRepeatingTask(Task task, int period) {
        return addTask(task, 0, period, false);
    }

    public TaskHandler scheduleRepeatingTask(Task task, int period, boolean asynchronous) {
        return addTask(task, 0, period, asynchronous);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Task task, int delay, int period) {
        return addTask(task, delay, period, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Task task, int delay, int period, boolean asynchronous) {
        return addTask(task, delay, period, asynchronous);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Runnable task, int delay, int period) {
        return addTask(null, task, delay, period, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Runnable task, int delay, int period, boolean asynchronous) {
        return addTask(null, task, delay, period, asynchronous);
    }

    public void cancelTask(int taskId) {
        if (taskMap.containsKey(taskId)) {
            try {
                taskMap.remove(taskId).cancel();
            } catch (RuntimeException ex) {
                Server.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
            }
        }
    }

    public void cancelTask(Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("Plugin cannot be null!");
        }
        for (Map.Entry<Integer, TaskHandler> entry : taskMap.entrySet()) {
            TaskHandler taskHandler = entry.getValue();
            if (plugin.equals(taskHandler.getPlugin())) {
                try {
                    taskHandler.cancel(); /* It will remove from task map automatic in next main heartbeat. */
                } catch (RuntimeException ex) {
                    Server.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
                }
            }
        }
    }

    public void cancelAllTasks() {
        for (Map.Entry<Integer, TaskHandler> entry : this.taskMap.entrySet()) {
            try {
                entry.getValue().cancel();
            } catch (RuntimeException ex) {
                Server.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
            }
        }
        this.taskMap.clear();
        this.queueMap.clear();
        this.currentTaskId.set(0);
    }

    public boolean isQueued(int taskId) {
        return this.taskMap.containsKey(taskId);
    }

    private TaskHandler addTask(Task task, int delay, int period, boolean asynchronous) {
        return addTask(task instanceof PluginTask ? ((PluginTask) task).getOwner() : null, () -> task.onRun(currentTick + delay), delay, period, asynchronous);
    }

    private TaskHandler addTask(Plugin plugin, Runnable task, int delay, int period, boolean asynchronous) {
        if (plugin != null && plugin.isDisabled()) {
            throw new PluginException("Plugin '" + plugin.getName() + "' attempted to register a task while disabled.");
        }
        if (delay < 0 || period < 0) {
            throw new PluginException("Attempted to register a task with negative delay or period.");
        }

        TaskHandler taskHandler = new TaskHandler(plugin, task, nextTaskId(), asynchronous);
        taskHandler.setDelay(delay);
        taskHandler.setPeriod(period);
        taskHandler.setNextRunTick(taskHandler.isDelayed() ? currentTick + taskHandler.getDelay() : currentTick);

        pending.offer(taskHandler);
        taskMap.put(taskHandler.getTaskId(), taskHandler);

        return taskHandler;
    }

    public void mainThreadHeartbeat(int currentTick) {
        // Accepts pending.
        TaskHandler task;
        while ((task = pending.poll()) != null) {
            int tick = Math.max(currentTick, task.getNextRunTick()); // Do not schedule in the past
            ArrayDeque<TaskHandler> queue = Utils.getOrCreate(queueMap, ArrayDeque.class, tick);
            queue.add(task);
        }
        if (currentTick - this.currentTick > queueMap.size()) { // A large number of ticks have passed since the last execution
            for (Map.Entry<Integer, ArrayDeque<TaskHandler>> entry : queueMap.entrySet()) {
                int tick = entry.getKey();
                if (tick <= currentTick) {
                    runTasks(tick);
                }
            }
        } else { // Normal server tick
            for (int i = this.currentTick + 1; i <= currentTick; i++) {
                runTasks(currentTick);
            }
        }
        this.currentTick = currentTick;
        AsyncTask.collectTask();
    }

    private void runTasks(int currentTick) {
        ArrayDeque<TaskHandler> queue = queueMap.remove(currentTick);
        if (queue != null) {
            for (TaskHandler taskHandler : queue) {
                if (taskHandler.isCancelled()) {
                    taskMap.remove(taskHandler.getTaskId());
                    continue;
                } else if (taskHandler.isAsynchronous()) {
                    asyncPool.submitTask(taskHandler.getTask());
                } else {
                    taskHandler.timing.startTiming();
                    try {
                        taskHandler.run(currentTick);
                    } catch (Throwable e) {
                        Server.getInstance().getLogger().critical("Could not execute taskHandler " + taskHandler.getTaskId() + ": " + e.getMessage());
                        Server.getInstance().getLogger().logException(e instanceof Exception ? (Exception) e : new RuntimeException(e));
                    }
                    taskHandler.timing.stopTiming();
                }
                if (taskHandler.isRepeating()) {
                    taskHandler.setNextRunTick(currentTick + taskHandler.getPeriod());
                    pending.offer(taskHandler);
                } else {
                    try {
                        taskMap.remove(taskHandler.getTaskId()).cancel();
                    } catch (RuntimeException ex) {
                        Server.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
                    }
                }
            }
        }
    }

    public int getQueueSize() {
        int size = pending.size();
        for (ArrayDeque<TaskHandler> queue : queueMap.values()) {
            size += queue.size();
        }
        return size;
    }

    private int nextTaskId() {
        return currentTaskId.incrementAndGet();
    }

}
