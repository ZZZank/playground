package eventbus.impl.dispatch;

import eventbus.EventBus;
import eventbus.EventListenerToken;
import eventbus.dispatch.DispatchKey;
import eventbus.dispatch.DispatchEventBus;
import eventbus.impl.EventBusImpl;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public class DispatchEventBusImpl<E, K> extends EventBusImpl<E> implements DispatchEventBus<E, K> {
    private final DispatchKey<E, K> dispatchKey;
    private final Map<K, EventBus<E>> dispatched;

    public DispatchEventBusImpl(
        Class<E> eventType,
        DispatchKey<E, K> dispatchKey,
        Map<K, EventBus<E>> dispatchBackend
    ) {
        super(eventType);
        this.dispatchKey = Objects.requireNonNull(dispatchKey);
        this.dispatched = Objects.requireNonNull(dispatchBackend);
    }

    @Override
    public final EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener) {
        return this.dispatched
            .computeIfAbsent(key, k -> new EventBusImpl<>(this.eventType()))
            .addListener(priority, listener);
    }

    @Override
    public final EventListenerToken<E> addListener(K key, Consumer<E> listener) {
        return addListener(key, (byte) 0, listener);
    }

    @Override
    public final DispatchKey<E, K> dispatchKey() {
        return dispatchKey;
    }

    @Override
    public boolean post(E event) {
        return DispatchEventBus.super.post(event);
    }

    @Override
    public final boolean post(E event, K key) {
        super.post(event);

        if (key != null) {
            var bus = this.dispatched.get(key);
            if (bus != null) {
                bus.post(event);
            }
        }
        return false;
    }
}
