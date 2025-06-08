package eventbus.dispatch;

import eventbus.CancellableEventBus;
import eventbus.EventListenerToken;
import eventbus.impl.dispatch.DispatchCancellableEventBusImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface DispatchCancellableEventBus<E, K> extends CancellableEventBus<E> {
    static <E, K> DispatchCancellableEventBus<E, K> create(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> backend
    ) {
        return new DispatchCancellableEventBusImpl<>(eventType, dispatchKey, backend);
    }

    static <E, K> DispatchCancellableEventBus<E, K> create(Class<E> eventType, EventDispatchKey<E, K> dispatchKey) {
        return create(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    EventDispatchKey<E, K> dispatchKey();

    EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener);

    EventListenerToken<E> addListener(K key, Predicate<E> listener);

    boolean post(E event, K key);

    @Override
    default boolean post(E event) {
        return post(event, this.dispatchKey().toKey(event));
    }
}
