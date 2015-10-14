package cn.nukkit.scheduler;

import cn.nukkit.plugin.Plugin;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Nukkit Project Team
 */
public class ScheduledTask {

    private int taskId;
    private boolean async;

    private Plugin plugin;
    private Runnable runnable;

    private int nextTick;
    private int daily;

    public int getTaskId() {
        return taskId;
    }

    public boolean isAsync() {
        return async;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public int getNextTick() {
        return nextTick;
    }

    public void setNextTick(int nextTick) {
        this.nextTick = nextTick;
    }

    public int getDaily() {
        return daily;
    }

    public void cancel() {
        nextTick = 0;
    }

    public void insert(List<ScheduledTask> list) {
        list.add(this);
    }

    public void reLoop(int currentTick) {
        nextTick = currentTick + daily;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setDaily(int daily) {
        this.daily = daily;
    }

    public void offerInto(PriorityQueue<ScheduledTask> pending) {
        pending.offer(this);
    }

}
