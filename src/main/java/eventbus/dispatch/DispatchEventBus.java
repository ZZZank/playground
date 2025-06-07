package eventbus.dispatch;

import eventbus.Event;
import eventbus.EventBus;
import eventbus.EventListenerToken;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface DispatchEventBus<E extends Event, K> extends EventBus<E> {

    EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener);

    EventListenerToken<E> addListener(K key, Consumer<E> listener);
}
