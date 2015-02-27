package com.chantake.MituyaProject.RSC.Event;

import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Dispatches bukkit events dynamically to interested listeners.
 * <p/>
 * To start the dispatcher you need to bind it to a plugin using the bindToPlugin method.
 * Calling stop() unregisters all event listeners.
 *
 * @author taleisenberg
 */
public class EventDispatcher {
    private final Map<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();
    private Plugin plugin;

    /**
     * Register an EventListener to a specific bukkit event.
     *
     * @param type The event type's class.
     * @param l    The registered EventListener.
     */
    public void registerListener(Class<? extends Event> type, EventListener l) {
        if (listeners.containsKey(type)) {
            List<EventListener> eventListeners = listeners.get(type);
            eventListeners.add(l);

        } else {
            List<EventListener> eventListeners = new ArrayList<>();
            eventListeners.add(l);
            registerBukkitListener(type);
            listeners.put(type, eventListeners);
        }
    }

    /**
     * Unregisters an EventListener.
     *
     * @param listener The unregistered listener
     * @return true when the listener is found in the registry or false otherwise.
     */
    public boolean unregisterListener(EventListener listener) {
        Set<Class<? extends Event>> events = lookupEventClasses(listener);
        for (Class<? extends Event> event : events) {
            List<EventListener> ls = listeners.get(event);
            if (ls.size() == 1) {
                unregisterBukkitListener(event);
                listeners.remove(event);
            } else {
                ls.remove(listener);
            }
        }

        return (!events.isEmpty());
    }

    /**
     * Bind the dispatcher to a plugin and start processing events.
     *
     * @param p The parent plugin of this dispatcher.
     */
    public void bindToPlugin(Plugin p) {
        this.plugin = p;

        listeners.keySet().forEach(this::registerBukkitListener);
    }

    /**
     * Unregister all bukkit event listeners and stop processing events.
     */
    public void stop() {
        listeners.keySet().forEach(this::unregisterBukkitListener);
    }

    private void registerBukkitListener(Class<? extends Event> eventClass) {
        if (plugin == null) return;

        plugin.getServer().getPluginManager().registerEvent(
                eventClass,
                new Listener() {
                },
                EventPriority.NORMAL,
                new DispatchExecutor(eventClass),
                plugin);
    }

    private void unregisterBukkitListener(Class<? extends Event> eventClass) {
        if (plugin == null) return;

        try {
            Method m = eventClass.getMethod("getHandlerList");
            HandlerList h = (HandlerList) m.invoke(null);
            h.unregister(plugin);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while unregistering event: " + e.getMessage());
        }
    }

    private Set<Class<? extends Event>> lookupEventClasses(EventListener eventListener) {
        Set<Class<? extends Event>> res = new HashSet<>();

        for (Class<? extends Event> e : listeners.keySet()) {
            for (EventListener l : listeners.get(e)) {
                if (eventListener == l) {
                    res.add(e);
                    break;
                }
            }
        }
        return res;
    }

    class DispatchExecutor implements EventExecutor {
        Class<? extends Event> eventClass;

        public DispatchExecutor(Class<? extends Event> eventClass) {
            this.eventClass = eventClass;
        }

        @Override
        public void execute(Listener l, Event event) throws EventException {
            for (EventListener el : listeners.get(eventClass)) {
                el.onEvent(event);
            }
        }
    }
}

