package config.prop;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import utils.Asser;

import java.util.*;

/**
 * @author ZZZank
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigProperty<T> {

    private static final Map<String, ConfigProperty<?>> NAMED = new LinkedHashMap<>();

    public static final ConfigProperty<List<String>> COMMENTS = register("comments", Collections.emptyList());
    public static final ConfigProperty<Collection<String>> ENUMS = register("enums", Collections.emptyList());
    public static final ConfigProperty<String> EXAMPLE = register("example", null);

    public static synchronized <T> ConfigProperty<T> register(String name, T defaultValue) {
        Asser.tNotNull(name, "name");
        if (NAMED.containsKey(name)) {
            throw new IllegalArgumentException("config property with name '" + name + "' already registered");
        }
        var prop = new ConfigProperty<>(name, NAMED.size(), defaultValue);
        NAMED.put(name, prop);
        return prop;
    }

    public static ConfigProperty<?> get(String name) {
        Asser.tNotNull(name, "name");
        return NAMED.get(name);
    }

    private final String name;
    private final int index;
    private final T defaultValue;

    public String name() {
        return this.name;
    }

    public int index() {
        return this.index;
    }

    public T defaultValue() {
        return this.defaultValue;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return "ConfigProperty{" + name + "}";
    }
}
