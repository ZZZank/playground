package eventbus.impl.dispatch;

import eventbus.EventBus;
import eventbus.dispatch.DispatchKey;
import eventbus.dispatch.DispatchEventBus;

import java.util.Map;

/**
 * @author ZZZank
 */
public final class DispatchEventBusImpl<E, K> extends DispatchEventBusBase<E, K, EventBus<E>> implements DispatchEventBus<E, K> {
    public DispatchEventBusImpl(Class<E> eventType, DispatchKey<E, K> dispatchKey, Map<K, EventBus<E>> dispatched) {
        super(eventType, dispatchKey, dispatched);
    }

    @Override
    protected EventBus<E> createBus(Class<E> eventType) {
        return EventBus.create(eventType);
    }
}
