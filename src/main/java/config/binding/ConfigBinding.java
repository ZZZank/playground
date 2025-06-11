package config.binding;

import org.jetbrains.annotations.NotNull;
import config.report.BuiltinResults;
import config.report.AccessResult;

/**
 * @author ZZZank
 */
public interface ConfigBinding<T> {

    @NotNull
    T getDefault();

    @NotNull
    Class<T> getDefaultType();

    @NotNull
    T get();

    default AccessResult<T> getSafe() {
        try {
            return AccessResult.good(get());
        } catch (Exception e) {
            return BuiltinResults.exception(e);
        }
    }

    @NotNull
    AccessResult<T> set(T value);
}
