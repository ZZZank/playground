package eventbus;

import caller.CallerClassGetter;
import eventbus.impl.EventBusImpl;

import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface CancellableEventBus<E> {
    static <E> EventBus<E> create(Class<E> eventType) {
        return new EventBusImpl<>(eventType);
    }

    Class<E> eventType();

    EventListenerToken<E> addListener(Predicate<E> listener);

    EventListenerToken<E> addListener(byte priority, Predicate<E> listener);

    /// @return `true` if at least one event listener returned `true`, `false` otherwise
    boolean post(E event);

    /// @return `true` if there's a registered listener matching this token, `false` otherwise
    boolean unregister(EventListenerToken<E> token);
}
