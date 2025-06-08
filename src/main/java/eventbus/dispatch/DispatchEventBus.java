package eventbus.dispatch;

import eventbus.EventBus;
import eventbus.EventListenerToken;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface DispatchEventBus<E, K> extends EventBus<E> {

    EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener);

    EventListenerToken<E> addListener(K key, Consumer<E> listener);
}
