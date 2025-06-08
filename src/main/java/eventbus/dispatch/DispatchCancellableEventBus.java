package eventbus.dispatch;

import eventbus.CancellableEventBus;
import eventbus.EventListenerToken;

import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface DispatchCancellableEventBus<E, K> extends CancellableEventBus<E> {

    EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener);

    EventListenerToken<E> addListener(K key, Predicate<E> listener);
}
