package eventbus.impl.dispatch;

import eventbus.CancellableEventBus;
import eventbus.EventListenerToken;
import eventbus.dispatch.DispatchCancellableEventBus;
import eventbus.dispatch.EventDispatchKey;
import eventbus.impl.CancellableEventBusImpl;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public class DispatchCancellableEventBusImpl<E, K> extends CancellableEventBusImpl<E>
    implements DispatchCancellableEventBus<E, K> {
    private final EventDispatchKey<E, K> dispatchKey;
    private final Map<K, CancellableEventBus<E>> dispatched;

    public DispatchCancellableEventBusImpl(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> dispatched
    ) {
        super(eventType);
        this.dispatchKey = Objects.requireNonNull(dispatchKey);
        this.dispatched = Objects.requireNonNull(dispatched);
    }

    @Override
    public EventDispatchKey<E, K> dispatchKey() {
        return dispatchKey;
    }

    @Override
    public EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener) {
        return this.dispatched
            .computeIfAbsent(key, k -> new CancellableEventBusImpl<>(this.eventType()))
            .addListener(priority, listener);
    }

    @Override
    public boolean post(E event, K key) {
        if (super.post(event)) {
            return true;
        }

        key = this.dispatchKey.transformInput(key);
        if (key != null) {
            var bus = this.dispatched.get(key);
            return bus != null && bus.post(event);
        }
        return false;
    }
}
