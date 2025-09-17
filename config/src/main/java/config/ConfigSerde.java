package config;

import config.impl.serde.UnarySerde;
import config.impl.serde.XMapSerde;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @param <I> intermediate object type
 * @param <T> object type at runtime
 * @author ZZZank
 */
public interface ConfigSerde<I, T> {

    static <T> ConfigSerde<T, T> unary() {
        return (ConfigSerde<T, T>) UnarySerde.INSTANCE;
    }

    @NotNull
    I serialize(@NotNull T value);

    @NotNull
    T deserialize(@NotNull I intermediate);

    default <I2> ConfigSerde<I2, T> xMap(Function<I, I2> reSerializer, Function<I2, I> reDeserializer) {
        return new XMapSerde<>(this, reSerializer, reDeserializer);
    }
}
