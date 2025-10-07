package config.binding;

import config.ConfigBinding;
import org.jetbrains.annotations.NotNull;
import utils.Asser;
import config.AccessResult;
import config.impl.report.BuiltinResults;

/**
 * @author ZZZank
 */
abstract class BindingBase<T> implements ConfigBinding<T> {

    @NotNull
    protected final T defaultValue;
    @NotNull
    protected final String name;

    protected BindingBase(@NotNull T defaultValue, @NotNull String name) {
        this.defaultValue = Asser.tNotNull(defaultValue, "defaultValue");
        this.name = Asser.tNotNull(name, "name");
    }

    @Override
    public @NotNull T getDefault() {
        return defaultValue;
    }

    @Override
    public @NotNull AccessResult<T> set(T value) {
        var validated = validate(value);
        if (validated.hasMessage()) {
            return validated;
        }
        try {
            setImpl(value);
            return AccessResult.none();
        } catch (Exception e) {
            return BuiltinResults.exception(e);
        }
    }

    abstract protected void setImpl(T value);

    public AccessResult<T> validate(T value) {
        if (value == null) {
            return BuiltinResults.nullValueError(name);
        }
        return AccessResult.none();
    }
}
