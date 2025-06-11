package config.report;

import asser.Asser;
import config.report.AccessResult.ResultType;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public class BuiltinResults {

    private static final Supplier<String> SUPPLY_NULL = () -> null;

    static final AccessResult<?> NONE = good(null);

    public static <T> AccessResult<T> good(T value) {
        return new AccessResultImpl<>(value, ResultType.GOOD, SUPPLY_NULL);
    }

    public static <T> AccessResult<T> info(T value, Supplier<String> message) {
        return new AccessResultImpl<>(value, ResultType.INFO, message);
    }

    public static <T> AccessResult<T> warning(T value, Supplier<String> message) {
        return new AccessResultImpl<>(value, ResultType.WARNING, message);
    }

    public static <T> AccessResult<T> error(Supplier<String> message) {
        return new AccessResultImpl<>(null, ResultType.ERROR, message);
    }

    public static <T> AccessResult<T> error(String message) {
        return error(() -> message);
    }

    public static <T> AccessResult<T> readOnlyError(String name) {
        return error(() -> String.format("config entry '%s' is readonly", name));
    }

    public static <T> AccessResult<T> nullValueError(String name) {
        return error(() -> String.format("config entry '%s' received a null value", name));
    }

    public static <T> AccessResult<T> outOfRangeError(String name, Object received, Object min, Object max) {
        return error(() -> String.format(
            "value %s for config entry '%s' not in range: [%s, %s]",
            received,
            name,
            min,
            max
        ));
    }

    public static <T> AccessResult<T> exception(Throwable throwable) {
        Asser.tNotNull(throwable, "throwable");
        return error(throwable::toString);
    }
}
