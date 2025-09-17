package config.impl.serde;

import config.ConfigSerde;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public record XMapSerde<I2, I, T>(
    ConfigSerde<I, T> backend,
    Function<I, I2> serializeBridge,
    Function<I2, I> deserializeBridge
) implements ConfigSerde<I2, T> {

    public XMapSerde(ConfigSerde<I, T> backend, Function<I, I2> serializeBridge, Function<I2, I> deserializeBridge) {
        this.backend = Objects.requireNonNull(backend);
        this.serializeBridge = Objects.requireNonNull(serializeBridge);
        this.deserializeBridge = Objects.requireNonNull(deserializeBridge);
    }

    @Override
    public @NotNull I2 serialize(@NotNull T value) {
        return serializeBridge.apply(backend.serialize(value));
    }

    @Override
    public @NotNull T deserialize(@NotNull I2 intermediate) {
        return backend.deserialize(deserializeBridge.apply(intermediate));
    }
}
