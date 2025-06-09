package eventbus.dispatch;

import eventbus.EventBus;
import eventbus.EventListenerToken;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface DispatchEventBus<E, K> extends EventBus<E> {

    EventDispatchKey<E, K> dispatchKey();

    EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener);

    EventListenerToken<E> addListener(K key, Consumer<E> listener);

    void post(E event, K key);

    @Override
    default void post(E event) {
        post(event, this.dispatchKey().toKey(event));
    }
}
