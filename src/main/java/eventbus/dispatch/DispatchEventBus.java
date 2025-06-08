package eventbus.dispatch;

import eventbus.EventBus;
import eventbus.EventListenerToken;
import eventbus.impl.dispatch.DispatchEventBusImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface DispatchEventBus<E, K> extends EventBus<E> {
    static <E, K> DispatchEventBus<E, K> create(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, EventBus<E>> backend
    ) {
        return new DispatchEventBusImpl<>(eventType, dispatchKey, backend);
    }

    static <E, K> DispatchEventBus<E, K> create(Class<E> eventType, EventDispatchKey<E, K> dispatchKey) {
        return create(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener);

    EventListenerToken<E> addListener(K key, Consumer<E> listener);
}
