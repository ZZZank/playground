package config.binding;

import org.jetbrains.annotations.NotNull;
import config.AccessResult;
import config.impl.report.BuiltinResults;

/**
 * @author ZZZank
 */
public class ReadOnlyBinding<T> extends BindingBase<T> {

    public ReadOnlyBinding(@NotNull T defaultValue, @NotNull String name) {
        super(defaultValue, name);
    }

    @Override
    public @NotNull T get() {
        return defaultValue;
    }

    @Override
    protected void setImpl(T value) {
    }

    @Override
    public AccessResult<T> validate(T value) {
        return BuiltinResults.readOnlyError(name);
    }
}
