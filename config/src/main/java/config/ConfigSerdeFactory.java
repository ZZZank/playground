package config;

import java.lang.reflect.*;

/**
 * @param <I> intermediate object type, used by {@link ConfigSerde}
 * @author ZZZank
 */
public interface ConfigSerdeFactory<I> {

    ConfigSerde<I, ?> getSerde(Type type);
}
