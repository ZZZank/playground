package eventbus.impl;

import eventbus.CancellableEventBus;
import eventbus.EventBus;
import eventbus.EventBusGroup;
import eventbus.dispatch.DispatchCancellableEventBus;
import eventbus.dispatch.DispatchEventBus;
import eventbus.dispatch.EventDispatchKey;
import eventbus.impl.dispatch.DispatchCancellableEventBusImpl;
import eventbus.impl.dispatch.DispatchEventBusImpl;
import lombok.val;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public record EventBusGroupImpl(
    String name,
    Map<Class<?>, EventBus<?>> busCache
) implements EventBusGroup {

    @Override
    public boolean hasBusFor(Class<?> eventType) {
        return eventType != null && busCache.containsKey(eventType);
    }

    private <E, T extends EventBus<?>> T ensureBus(
        Class<T> busType,
        Class<E> eventType,
        Function<Class<?>, T> computeFn
    ) {
        val got = busCache.computeIfAbsent(eventType, computeFn);
        if (!busType.isInstance(got)) {
            throw new IllegalArgumentException(String.format(
                "An eventbus for '%s' already existed and not an instance of '%s'",
                eventType.getName(),
                busType.getName()
            ));
        }
        return (T) got;
    }

    @Override
    public <E> EventBus<E> ofBus(Class<E> eventType) {
        return ensureBus(EventBus.class, eventType, EventBusImpl::new);
    }

    @Override
    public <E> CancellableEventBus<E> ofCancellableBus(Class<E> eventType) {
        return ensureBus(CancellableEventBus.class, eventType, CancellableEventBusImpl::new);
    }

    @Override
    public <E, K> DispatchEventBus<E, K> ofDispatchBus(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, EventBus<E>> backend
    ) {
        return ensureBus(
            DispatchEventBus.class,
            eventType,
            ignored -> new DispatchEventBusImpl<>(eventType, dispatchKey, backend)
        );
    }

    @Override
    public <E, K> DispatchCancellableEventBus<E, K> ofDispatchCancellableBus(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> backend
    ) {
        return ensureBus(
            DispatchCancellableEventBus.class,
            eventType,
            ignored -> new DispatchCancellableEventBusImpl<>(eventType, dispatchKey, backend)
        );
    }

    @Override
    public Collection<? extends EventBus<?>> viewBuses() {
        return Collections.unmodifiableCollection(this.busCache.values());
    }
}
