package eventbus.impl.dispatch;

import eventbus.EventBus;
import eventbus.EventListenerToken;
import eventbus.dispatch.EventDispatchKey;
import eventbus.dispatch.DispatchEventBus;
import eventbus.impl.EventBusImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public class DispatchEventBusImpl<E, K> extends EventBusImpl<E> implements DispatchEventBus<E, K> {
    private final EventDispatchKey<E, K> dispatchKey;
    private final Map<K, EventBus<E>> dispatched;

    public DispatchEventBusImpl(Class<E> eventType, EventDispatchKey<E, K> dispatchKey) {
        super(eventType);
        this.dispatchKey = dispatchKey;
        this.dispatched = new HashMap<>();
    }

    @Override
    public EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener) {
        return this.dispatched
            .computeIfAbsent(key, k -> new EventBusImpl<>(this.eventType()))
            .addListener(priority, listener);
    }

    @Override
    public EventListenerToken<E> addListener(K key, Consumer<E> listener) {
        return addListener(key, (byte) 0, listener);
    }

    @Override
    public void post(E event) {
        super.post(event);
        var bus = this.dispatched.get(this.dispatchKey.toKey(event));
        if (bus != null) {
            bus.post(event);
        }
    }
}
