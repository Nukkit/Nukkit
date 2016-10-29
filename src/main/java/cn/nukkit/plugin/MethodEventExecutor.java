package cn.nukkit.plugin;

import cn.nukkit.event.Event;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.EventException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MethodEventExecutor implements EventExecutor {

    private final Method method;

    public MethodEventExecutor(Method method) {
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            method.invoke(listener, event);
        } catch (InvocationTargetException ex) {
            throw new EventException(ex.getCause());
        } catch (ClassCastException ex) {
            // We shouldn't ignore it, it's a valid error.
            // If the method shouldn't be called, don't register it instead of registering it, calling it and catching the exception
            throw new EventException(ex.getCause());
        } catch (Throwable t) {
            throw new EventException(t);
        }
    }

    public Method getMethod() {
        return method;
    }
}