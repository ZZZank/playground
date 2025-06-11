package config.binding;

import config.report.BuiltinResults;
import org.jetbrains.annotations.NotNull;
import utils.Asser;
import config.report.AccessResult;
import config.struct.ConfigRoot;

import java.io.IOException;

/**
 * @author ZZZank
 */
public class AutoSaveBinding<T> implements ConfigBinding<T> {
    private final ConfigBinding<T> inner;
    private final ConfigRoot root;

    public AutoSaveBinding(ConfigBinding<T> inner, ConfigRoot root) {
        this.inner = Asser.tNotNull(inner, "inner binding");
        this.root = Asser.tNotNull(root, "config root");
    }

    @Override
    public @NotNull T getDefault() {
        return inner.getDefault();
    }

    @Override
    public @NotNull Class<T> getDefaultType() {
        return inner.getDefaultType();
    }

    @Override
    public @NotNull T get() {
        return inner.get();
    }

    @Override
    public AccessResult<T> getSafe() {
        return inner.getSafe();
    }

    @Override
    public @NotNull AccessResult<T> set(T value) {
        var result = inner.set(value);
        try {
            root.save();
        } catch (IOException e) {
            return BuiltinResults.error(() -> "Unable to save config file: " + e);
        }
        return result;
    }
}
