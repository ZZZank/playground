package eventbus.dispatch;

import java.util.Optional;

/**
 * @author ZZZank
 */
public interface EventDispatchKey<E, K> {
    Class<K> keyType();

    K toKey(E event);

    default Optional<K> wrapInput(Object input) {
        return keyType().isInstance(input) ? Optional.of((K) input) : Optional.empty();
    }
}
