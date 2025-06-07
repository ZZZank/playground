package eventbus.impl;

import eventbus.Event;
import eventbus.EventBus;

import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * @author ZZZank
 */
public class EventBusImpl<E extends Event> extends EventBusBase<E, Consumer<E>> implements EventBus<E> {
    private static final IntFunction GENERATOR = Consumer[]::new;

    public EventBusImpl(Class<E> eventType) {
        super(eventType);
    }

    @Override
    public void post(E event) {
        for (var consumer : getBuilt(GENERATOR)) {
            consumer.accept(event);
        }
    }
}
