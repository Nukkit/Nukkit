package cn.nukkit.plugin;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.timings.Timing;
import cn.nukkit.utils.EventException;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RegisteredListener {

    private final Listener listener;

    private final EventPriority priority;

    private final Plugin plugin;

    private final EventExecutor executor;

    private final Timing timing;

    private final boolean ignoreCancelled;

    private eventCaller caller;

    public RegisteredListener(Listener listener, EventExecutor executor, EventPriority priority, Plugin plugin, boolean ignoreCancelled, Timing timing) {
        this.listener = listener;
        this.priority = priority;
        this.plugin = plugin;
        this.executor = executor;
        this.timing = timing;
        this.ignoreCancelled = ignoreCancelled;
        // Avoid checking cancellation
        if (ignoreCancelled) {
            caller = new eventCaller() {
                @Override
                public void callEvent(Event event) {
                    if (event instanceof Cancellable) {
                        if (event.isCancelled() && isIgnoringCancelled()) {
                            return;
                        }
                    }
                    super.callEvent(event);
                }
            };
        } else {
            caller = new eventCaller();
        }
    }

    public Listener getListener() {
        return listener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public void callEvent(Event event) throws EventException {
        caller.callEvent(event);
    }

    public boolean isIgnoringCancelled() {
        return ignoreCancelled;
    }

    public class eventCaller {
        public void callEvent(Event event) {
            timing.startTiming();
            executor.execute(listener, event);
            timing.stopTiming();
        }
    }
}
