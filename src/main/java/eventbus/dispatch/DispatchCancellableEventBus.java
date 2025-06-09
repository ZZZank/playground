package eventbus.dispatch;

import eventbus.CancellableEventBus;
import eventbus.EventListenerToken;

import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface DispatchCancellableEventBus<E, K> extends CancellableEventBus<E> {

    EventDispatchKey<E, K> dispatchKey();

    EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener);

    default EventListenerToken<E> addListener(K key, Predicate<E> listener) {
        return addListener(key, (byte) 0, listener);
    }

    boolean post(E event, K key);

    @Override
    default boolean post(E event) {
        return post(event, this.dispatchKey().toKey(event));
    }
}
