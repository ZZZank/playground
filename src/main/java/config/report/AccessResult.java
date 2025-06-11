package config.report;

import utils.Asser;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public interface AccessResult<T> {

    static <T> AccessResult<T> none() {
        return (AccessResult<T>) BuiltinResults.NONE;
    }

    static <T> AccessResult<T> good(T value) {
        return new AccessResultImpl<>(value, ResultType.GOOD, BuiltinResults.SUPPLY_NULL);
    }

    static <T> AccessResult<T> info(T value, Supplier<String> message) {
        return new AccessResultImpl<>(value, ResultType.INFO, message);
    }

    static <T> AccessResult<T> warning(T value, Supplier<String> message) {
        return new AccessResultImpl<>(value, ResultType.WARNING, message);
    }

    static <T> AccessResult<T> error(Supplier<String> message) {
        return new AccessResultImpl<>(null, ResultType.ERROR, message);
    }

    T value();

    ResultType type();

    String message();

    default boolean hasValue() {
        return value() != null;
    }

    default Optional<T> valueOptional() {
        return Optional.ofNullable(value());
    }

    default T valueOr(T fallback) {
        var value = value();
        return value == null ? fallback : value;
    }

    default T valueOrGet(Supplier<T> fallback) {
        Asser.tNotNull(fallback, "fallback value provider");
        var value = value();
        return value == null ? fallback.get() : value;
    }

    default Optional<String> messageOptional() {
        return Optional.ofNullable(message());
    }

    default boolean hasMessage() {
        return type() != ResultType.GOOD;
    }

    enum ResultType {
        GOOD,
        INFO,
        WARNING,
        ERROR
    }
}
