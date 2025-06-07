package eventbus.dispatch;

import eventbus.Event;

import java.util.Optional;

/**
 * @author ZZZank
 */
public interface EventDispatchKey<E extends Event, K> {
    Class<K> keyType();

    K toKey(E event);

    default Optional<K> wrapInput(Object input) {
        return keyType().isInstance(input) ? Optional.of((K) input) : Optional.empty();
    }
}
