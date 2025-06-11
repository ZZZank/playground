package config.report;

import utils.Asser;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public class BuiltinResults {

    static final Supplier<String> SUPPLY_NULL = () -> null;

    static final AccessResult<?> NONE = AccessResult.good(null);

    public static <T> AccessResult<T> readOnlyError(String name) {
        return AccessResult.error(() -> String.format("config entry '%s' is readonly", name));
    }

    public static <T> AccessResult<T> nullValueError(String name) {
        return AccessResult.error(() -> String.format("config entry '%s' received a null value", name));
    }

    public static <T> AccessResult<T> outOfRangeError(String name, Object received, Object min, Object max) {
        return AccessResult.error(() -> String.format(
            "value %s for config entry '%s' not in range: [%s, %s]",
            received,
            name,
            min,
            max
        ));
    }

    public static <T> AccessResult<T> exception(Throwable throwable) {
        Asser.tNotNull(throwable, "throwable");
        return AccessResult.error(throwable::toString);
    }
}
