package config.impl.serde;

import config.ConfigSerde;
import org.jetbrains.annotations.NotNull;

/**
 * @author ZZZank
 */
public enum UnarySerde implements ConfigSerde<Object, Object> {
    INSTANCE;

    @Override
    public @NotNull Object serialize(@NotNull Object value) {
        return value;
    }

    @Override
    public @NotNull Object deserialize(@NotNull Object intermediate) {
        return intermediate;
    }
}
