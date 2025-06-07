package eventbus.impl;

import eventbus.CancellableEventBus;
import eventbus.Event;

import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public class CancellableEventBusImpl<E extends Event>
    extends EventBusBase<E, Predicate<E>> implements CancellableEventBus<E> {
    private static final IntFunction GENERATOR = Predicate[]::new;

    public CancellableEventBusImpl(Class<E> eventType) {
        super(eventType);
    }

    @Override
    public boolean post(E event) {
        for (var predicate : getBuilt(GENERATOR)) {
            if (predicate.test(event)) {
                return true;
            }
        }
        return false;
    }
}
