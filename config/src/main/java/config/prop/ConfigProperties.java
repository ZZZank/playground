package config.prop;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public class ConfigProperties {
    private final Map<ConfigProperty<?>, Object> internal;

    public ConfigProperties() {
        this(ConcurrentHashMap::new);
    }

    public ConfigProperties(Supplier<Map<ConfigProperty<?>, Object>> backend) {
        internal = backend.get();
    }

    public <T> void put(ConfigProperty<T> property, T value) {
        Objects.requireNonNull(property);
        Objects.requireNonNull(value);
        internal.put(property, value);
    }

    public <T> T merge(ConfigProperty<T> property, T value, BiFunction<? super T, ? super T, ? extends T> merger) {
        return (T) internal.merge(property, value, (BiFunction) merger);
    }

    public <T> T getNullable(ConfigProperty<T> property) {
        return (T) internal.get(property);
    }

    public <T> T getOrDefault(ConfigProperty<T> property) {
        var got = getNullable(property);
        return got == null ? property.defaultValue() : got;
    }

    public <T> Optional<T> get(ConfigProperty<T> property) {
        return Optional.ofNullable(getNullable(property));
    }

    public <T> boolean has(ConfigProperty<T> property) {
        return internal.containsKey(property);
    }

    public <T> T remove(ConfigProperty<T> property) {
        return (T) internal.remove(property);
    }
}
