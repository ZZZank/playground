package eventbus.impl;

import eventbus.Event;
import eventbus.EventListenerToken;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * @author ZZZank
 */
public abstract class EventBusBase<EVENT extends Event, LISTENER> {
    private final Class<EVENT> eventType;
    private final List<EventListenerTokenImpl<EVENT, LISTENER>> tokens;
    private volatile LISTENER[] built;

    protected EventBusBase(Class<EVENT> eventType) {
        this.eventType = eventType;
        this.tokens = new ArrayList<>();
    }

    public final Class<EVENT> eventType() {
        return eventType;
    }

    public final EventListenerToken<EVENT> addListener(LISTENER listener) {
        return addListener((byte) 0, listener);
    }

    public final EventListenerToken<EVENT> addListener(byte priority, LISTENER listener) {
        built = null;
        var token = new EventListenerTokenImpl<>(eventType, priority, listener);
        tokens.add(token);
        return token;
    }

    public final boolean unregister(EventListenerToken<EVENT> token) {
        var changed = this.tokens.remove(token);
        if (changed) {
            built = null;
        }
        return changed;
    }

    protected final LISTENER[] getBuilt(IntFunction<LISTENER[]> arrayGenerator) {
        if (built == null) {
            synchronized (this) {
                if (built == null) {
                    tokens.sort(null);
                    built = tokens.stream()
                        .map(EventListenerTokenImpl::listener)
                        .toArray(arrayGenerator);
                }
            }
        }
        return built;
    }
}
