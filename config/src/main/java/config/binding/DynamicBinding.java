package config.binding;

import org.jetbrains.annotations.NotNull;
import utils.Asser;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public class DynamicBinding<T> extends BindingBase<T> {
    @NotNull
    public final Supplier<T> getter;
    @NotNull
    public final Consumer<T> setter;

    public DynamicBinding(@NotNull T defaultValue, @NotNull String name, @NotNull Supplier<T> getter, @NotNull Consumer<T> setter) {
        super(defaultValue, name);
        this.getter = Asser.tNotNull(getter, "getter");
        this.setter = Asser.tNotNull(setter, "setter");
    }

    @Override
    public @NotNull T get() {
        return getter.get();
    }

    @Override
    protected void setImpl(T value) {
        setter.accept(value);
    }
}
