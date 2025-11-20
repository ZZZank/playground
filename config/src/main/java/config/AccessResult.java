package config;

import config.impl.report.AccessResultImpl;
import config.impl.report.BuiltinResults;
import config.impl.report.GoodAccessResult;
import utils.Asser;

import java.util.Objects;
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
        return new GoodAccessResult<>(value);
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

    static <T> AccessResult<T> error(Throwable throwable) {
        Objects.requireNonNull(throwable);
        return error(() -> {
            var className = throwable.getClass().getSimpleName();
            var message = throwable.getLocalizedMessage();
            return message == null ? className : className + ": " + message;
        });
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
