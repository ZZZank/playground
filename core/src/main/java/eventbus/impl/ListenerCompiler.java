package eventbus.impl;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public class ListenerCompiler {

    public static <E> Predicate<E> compile(Predicate<E>[] predicates) {
        switch (predicates.length) {
            case 0:
                return (ignored) -> false;
            case 1:
                return predicates[0];
            case 2:
                return predicates[0].or(predicates[1]);
            case 3:
                var p1 = predicates[0];
                var p2 = predicates[1];
                var p3 = predicates[2];
                return event -> p1.test(event) || p2.test(event) || p3.test(event);
            default:
                return event -> {
                    for (var predicate : predicates) {
                        if (predicate.test(event)) {
                            return true;
                        }
                    }
                    return false;
                };
        }
    }

    public static <E> Consumer<E> compile(Consumer<E>[] consumers) {
        switch (consumers.length) {
            case 0:
                return (ignored) -> {};
            case 1:
                return consumers[0];
            case 2:
                return consumers[0].andThen(consumers[1]);
            case 3:
                var c1 = consumers[0];
                var c2 = consumers[1];
                var c3 = consumers[2];
                return event -> {
                    c1.accept(event);
                    c2.accept(event);
                    c3.accept(event);
                };
            default:
                return event -> {
                    for (var consumer : consumers) {
                        consumer.accept(event);
                    }
                };
        }
    }
}
