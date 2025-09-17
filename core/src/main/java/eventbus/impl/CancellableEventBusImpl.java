package eventbus.impl;

import eventbus.CancellableEventBus;
import eventbus.CommonPriority;
import eventbus.EventListenerToken;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author ZZZank
 */
public final class CancellableEventBusImpl<E>
    extends EventBusBase<E, Predicate<E>> implements CancellableEventBus<E> {

    public CancellableEventBusImpl(Class<E> eventType) {
        super(eventType);
    }

    @Override
    public EventListenerToken<E> addListener(byte priority, Consumer<E> listener) {
        return addListener(priority, new NeverCancelListener<>(listener));
    }

    @Override
    public EventListenerToken<E> addListener(Consumer<E> listener) {
        return addListener(CommonPriority.NORMAL, new NeverCancelListener<>(listener));
    }

    @Override
    public boolean post(E event) {
        return getBuilt(CancellableEventBusImpl::compile).test(event);
    }

    private static <E> Predicate<E> compile(Stream<Predicate<E>> predicateStream) {
        var predicates = predicateStream.toArray((IntFunction<Predicate<E>[]>) Predicate[]::new);

        var allConsumer = Arrays.stream(predicates)
            .allMatch(predicate -> predicate instanceof NeverCancelListener<E>);
        if (allConsumer) {
            var consumers = (Consumer<E>[]) new Consumer[predicates.length];
            for (var i = 0; i < consumers.length; i++) {
                consumers[i] = ((NeverCancelListener<E>) predicates[i]).consumer();
            }
            return new NeverCancelListener<>(ListenerCompiler.compile(consumers));
        }

        return ListenerCompiler.compile(predicates);
    }
}
