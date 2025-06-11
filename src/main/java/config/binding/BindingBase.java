package config.binding;

import org.jetbrains.annotations.NotNull;
import utils.Asser;
import config.report.AccessResult;
import config.report.BuiltinResults;

/**
 * @author ZZZank
 */
public abstract class BindingBase<T> implements ConfigBinding<T> {

    @NotNull
    protected final T defaultValue;
    @NotNull
    protected final Class<T> defaultType;
    @NotNull
    protected final String name;

    protected BindingBase(@NotNull T defaultValue, @NotNull Class<T> defaultType, @NotNull String name) {
        this.defaultValue = Asser.tNotNull(defaultValue, "defaultValue");
        this.defaultType = Asser.tNotNull(defaultType, "defaultType");
        this.name = Asser.tNotNull(name, "name");
    }

    @Override
    public @NotNull T getDefault() {
        return defaultValue;
    }

    @Override
    public @NotNull Class<T> getDefaultType() {
        return defaultType;
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
