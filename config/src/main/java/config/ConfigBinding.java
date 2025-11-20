package config;

import org.jetbrains.annotations.NotNull;
import config.impl.report.BuiltinResults;

/**
 * @author ZZZank
 */
public interface ConfigBinding<T> {

    @NotNull
    T getDefault();

    @NotNull
    T get();

    default AccessResult<T> getSafe() {
        try {
            return AccessResult.good(get());
        } catch (Exception e) {
            return AccessResult.error(e);
        }
    }

    @NotNull
    AccessResult<T> set(T value);
}
