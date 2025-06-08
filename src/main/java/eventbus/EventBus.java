package eventbus;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface EventBus<E> {
    Class<E> eventType();

    EventListenerToken<E> addListener(Consumer<E> listener);

    EventListenerToken<E> addListener(byte priority, Consumer<E> listener);

    void post(E event);

    boolean unregister(EventListenerToken<E> token);
}
