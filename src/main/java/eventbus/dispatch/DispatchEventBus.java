package eventbus.dispatch;

import eventbus.EventBus;
import eventbus.EventListenerToken;
import eventbus.impl.dispatch.DispatchEventBusImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface DispatchEventBus<E, K> extends EventBus<E> {

    static <E, K> DispatchEventBus<E, K> create(Class<E> eventType, DispatchKey<E, K> dispatchKey) {
        return new DispatchEventBusImpl<>(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    DispatchKey<E, K> dispatchKey();

    EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener);

    EventListenerToken<E> addListener(K key, Consumer<E> listener);

    boolean post(E event, K key);

    @Override
    default boolean post(E event) {
        return post(event, this.dispatchKey().eventToKey(event));
    }
}
