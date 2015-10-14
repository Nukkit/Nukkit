package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Nukkit Project Team
 */
public class Scheduler {

    private final Server server;
    private final PriorityQueue<ScheduledTask> pending;
    private final List<ScheduledTask> dailyList;
    private final AsyncPool asyncPool;

    private int currentTick;
    private int currentTaskId;

    public Scheduler(Server server) {
        this.server = server;
        this.pending = new PriorityQueue<>((i, j) -> i.getNextTick() - j.getNextTick());
        this.dailyList = new ArrayList<>();
        this.asyncPool = new AsyncPool();
    }

    public ScheduledTask schedule(Plugin plugin, Runnable runnable) {
        return schedule(plugin, runnable, false);
    }

    public ScheduledTask schedule(Plugin plugin, Runnable runnable, boolean async) {
        return schedule(plugin, runnable, 0, async);
    }

    public ScheduledTask schedule(Plugin plugin, Runnable runnable, int delay) {
        return schedule(plugin, runnable, delay, false);
    }

    public ScheduledTask schedule(Plugin plugin, Runnable runnable, int delay, boolean async) {
        return schedule(plugin, runnable, delay, 0, async);
    }

    public ScheduledTask schedule(Plugin plugin, Runnable runnable, int delay, int daily) {
        return schedule(plugin, runnable, delay, daily, false);
    }

    public ScheduledTask schedule(Plugin plugin, Runnable runnable, int delay, int daily, boolean async) {
        if (runnable == null) {
            throw new NullPointerException("Runnable cannot be bull!");
        } else if (plugin != null && plugin.isDisabled()) {
            throw new RuntimeException("A disabled plugin try to schedule a task!");
        }
        ScheduledTask task = new ScheduledTask();
        task.setPlugin(plugin);
        task.setRunnable(runnable);
        task.setNextTick(currentTick + delay);
        task.setDaily(daily);
        task.setAsync(async);
        synchronized (this) {
            task.setTaskId(currentTaskId++);
            task.offerInto(pending);
        }
        return task;
    }

    @Deprecated
    public void scheduleAsync(AsyncTask task) {
        schedule(null, task, 0, 0, true);
    }

    public void cancel(int id) {
        synchronized (this) {
            pending.removeIf(task -> task.getTaskId() == id);
        }
    }

    public void cancel(Plugin plugin) {
        synchronized (this) {
            pending.removeIf(task -> task.getPlugin() == plugin);
        }
    }

    public void cancel() {
        synchronized (this) {
            pending.clear();
        }
    }

    public void mainThreadHeartbeat(int serverTick) {
        currentTick = serverTick;
        dailyList.clear();
        while (pendingNext()) {
            pending(poll());
        }
        addAll(dailyList);
    }

    private void addAll(List<ScheduledTask> list) {
        synchronized (this) {
            pending.addAll(list);
        }
    }

    private synchronized ScheduledTask poll() {
        return pending.poll();
    }

    private boolean pendingNext() {
        return pending.peek() != null && pending.peek().getNextTick() <= currentTick;
    }

    private void pending(ScheduledTask task) {
        if (!task.isCancelled()) {
            execute(task);
            if (task.getDaily() > 0) {
                task.reLoop(currentTick);
                task.insert(dailyList);
            }
        }
    }

    private void execute(ScheduledTask task) {
        if (task.isAsync()) {
            asyncPool.execute(task.getRunnable());
        } else try {
            task.getRunnable().run();
        } catch (Exception e) {
            server.getLogger().logException(e);
        }
    }

}
