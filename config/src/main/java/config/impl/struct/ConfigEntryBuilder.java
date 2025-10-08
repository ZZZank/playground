package config.impl.struct;

import config.ConfigBinding;
import config.ConfigCategory;
import config.ConfigEntry;
import org.jetbrains.annotations.NotNull;
import utils.Asser;
import utils.Cast;
import config.binding.*;
import config.prop.ConfigProperties;
import config.prop.ConfigProperty;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author ZZZank
 */
public class ConfigEntryBuilder<T> {

    private static final Pattern MATCH_LINE_BREAK = Pattern.compile("\n");
    public final ConfigCategory parent;
    public final String name;
    private ConfigBinding<T> binding;
    private final ConfigProperties properties = new ConfigProperties();
    private Type defaultType = null;

    public ConfigEntryBuilder(ConfigCategory parent, String name) {
        this.parent = Asser.tNotNull(parent, "parent category");
        this.name = Asser.tNotNull(name, "name");
    }

    public <T_> ConfigEntryBuilder<T_> bind(ConfigBinding<T_> binding) {
        var casted = Cast.<ConfigEntryBuilder<T_>>to(this);
        casted.binding = Asser.tNotNull(binding, "config binding");
        return casted;
    }

    public <T_> ConfigEntryBuilder<T_> bind(Function<String, ConfigBinding<T_>> toBinding) {
        var binding = toBinding.apply(name);
        return bind(binding);
    }

    public <T_> ConfigEntryBuilder<T_> bind(@NotNull Supplier<@NotNull T_> getter, @NotNull Consumer<T_> setter) {
        var defaultValue = getter.get();
        return bind(new DynamicBinding<>(defaultValue, name, getter, setter));
    }

    public <T_> ConfigEntryBuilder<T_> bindDefault(@NotNull T_ defaultValue) {
        return bind(new DefaultBinding<>(defaultValue, name));
    }

    public <T_> ConfigEntryBuilder<T_> bindReadOnly(@NotNull T_ defaultValue) {
        return bind(new ReadOnlyBinding<>(defaultValue, name));
    }

    public <T_ extends Comparable<T_>> ConfigEntryBuilder<T_> bindRanged(
        @NotNull T_ defaultValue,
        @NotNull T_ min,
        @NotNull T_ max
    ) {
        return bind(new RangedBinding<>(defaultValue, name, min, max));
    }

    public ConfigEntryBuilder<T> makeAutoSave() {
        return bind(new AutoSaveBinding<>(this.binding, this.parent.getRoot()));
    }

    public ConfigEntryBuilder<T> setDefaultType(Type type) {
        this.defaultType = type;
        return this;
    }

    public ConfigEntryBuilder<T> setDefaultTypeFromBinding() {
        var value = this.binding.getDefault();
        var type = value instanceof Enum<?> e ? e.getDeclaringClass() : value.getClass();
        return setDefaultType(type);
    }

    public <T_> ConfigEntryBuilder<T> setProperty(ConfigProperty<T_> property, @NotNull T_ value) {
        properties.put(property, value);
        return this;
    }

    public ConfigEntryBuilder<T> setComments(List<String> comments) {
        return setProperty(ConfigProperty.COMMENTS, comments);
    }

    public ConfigEntryBuilder<T> comment(String... comments) {
        this.properties.merge(
            ConfigProperty.COMMENTS,
            Arrays.stream(comments)
                .map(MATCH_LINE_BREAK::split)
                .flatMap(Arrays::stream)
                .toList(),
            (a, b) -> {
                a.addAll(b);
                return a;
            }
        );
        return this;
    }

    public ConfigEntry<T> build() {
        return this.parent.register(new ConfigEntryImpl<>(name, binding, properties, this.parent, this.defaultType));
    }
}
