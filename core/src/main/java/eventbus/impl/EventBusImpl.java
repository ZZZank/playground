package eventbus.impl;

import eventbus.EventBus;

import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @author ZZZank
 */
public final class EventBusImpl<E> extends EventBusBase<E, Consumer<E>> implements EventBus<E> {

    public EventBusImpl(Class<E> eventType) {
        super(eventType);
    }

    @Override
    public boolean post(E event) {
        getBuilt(EventBusImpl::compile).accept(event);
        return false;
    }

    private static <E> Consumer<E> compile(Stream<Consumer<E>> consumerStream) {
        var consumers = consumerStream.toArray((IntFunction<Consumer<E>[]>) Consumer[]::new);
        return ListenerCompiler.compile(consumers);
    }
}
