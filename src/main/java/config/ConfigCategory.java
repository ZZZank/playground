package config;

import config.prop.ConfigProperties;
import config.struct.ConfigCategoryImpl;
import config.struct.ConfigEntryBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public interface ConfigCategory extends ConfigEntry<Map<String, ConfigEntry<?>>> {

    char PATH_SPLITTER_CHAR = '.';

    String PATH_SPLITTER = "\\.";

    default ConfigEntry<?> getEntry(String path) {
        if (path.isEmpty()) {
            return this;
        }
        ConfigEntry<?> entry = this;
        for (var s : path.split(PATH_SPLITTER)) {
            entry = entry.asCategory().get().get(s);
        }
        return entry;
    }

    @Override
    default AccessResult<Map<String, ConfigEntry<?>>> set(Map<String, ConfigEntry<?>> value) {
        return AccessResult.error(() -> "internal container of ConfigCategory should not be mutated externally");
    }

    @Override
    default boolean isCategory() {
        return true;
    }

    @Override
    default String path() {
        var parent = parent();
        return parent == null ? name() : parent.path() + PATH_SPLITTER_CHAR + name();
    }

    default ConfigEntryBuilder<Void> define(String name) {
        return new ConfigEntryBuilder<>(this, name);
    }

    <T extends ConfigEntry<?>> T register(T entry);

    /// create an empty sub category
    default ConfigCategory subCategory(String name, Supplier<Map<String, ConfigEntry<?>>> mapStructure) {
        return register(new ConfigCategoryImpl(name, mapStructure, new ConfigProperties(), this));
    }

    /// create an empty sub category
    default ConfigCategory subCategory(String name) {
        return subCategory(name, LinkedHashMap::new);
    }
}
