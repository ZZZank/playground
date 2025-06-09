package eventbus;

import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface CancellableEventBus<E> extends EventBus<E> {

    EventListenerToken<E> addListener(Predicate<E> listener);

    EventListenerToken<E> addListener(byte priority, Predicate<E> listener);
}
