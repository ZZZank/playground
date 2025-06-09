package eventbus;

import eventbus.dispatch.DispatchCancellableEventBus;
import eventbus.dispatch.DispatchEventBus;
import eventbus.dispatch.EventDispatchKey;
import eventbus.impl.EventBusGroupImpl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZZZank
 */
public interface EventBusGroup {
    EventBusGroup DEFAULT = create("default");

    static EventBusGroup create(String name) {
        return new EventBusGroupImpl(name, new ConcurrentHashMap<>());
    }

    String name();

    boolean hasBusFor(Class<?> eventType);

    <E> EventBus<E> ofBus(Class<E> eventType);

    <E> CancellableEventBus<E> ofCancellableBus(Class<E> eventType);

    <E, K> DispatchEventBus<E, K> ofDispatchBus(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, EventBus<E>> backend
    );

    default <E, K> DispatchEventBus<E, K> ofDispatchBus(Class<E> eventType, EventDispatchKey<E, K> dispatchKey) {
        return ofDispatchBus(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    <E, K> DispatchCancellableEventBus<E, K> ofDispatchCancellableBus(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> backend
    );

    default <E, K> DispatchCancellableEventBus<E, K> ofDispatchCancellableBus(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey
    ) {
        return ofDispatchCancellableBus(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    Collection<? extends EventBus<?>> viewBuses();
}
