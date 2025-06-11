package config;

import config.impl.struct.ConfigRootImpl;
import config.prop.ConfigProperties;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public interface ConfigRoot extends ConfigCategory {

    static ConfigRoot of(ConfigIO io, @Nullable Path saveTo) {
        return new ConfigRootImpl(io, saveTo);
    }

    static ConfigRoot of(ConfigIO io, @Nullable Path saveTo, Supplier<Map<String, ConfigEntry<?>>> backend) {
        return new ConfigRootImpl(backend, new ConfigProperties(), io, saveTo);
    }

    ConfigIO io();

    Path filePath();

    default boolean inMemoryOnly() {
        return filePath() == null;
    }

    default void save() throws IOException {
        if (inMemoryOnly()) {
            return;
        }
        io().save(this, filePath());
    }

    default void read() throws IOException {
        if (inMemoryOnly() || !Files.exists(filePath())) {
            return;
        }
        io().read(this, filePath());
    }

    @Override
    default boolean isRoot() {
        return true;
    }

    @Override
    default ConfigRoot getRoot() {
        return this;
    }

    @Override
    default ConfigCategory parent() {
        return null;
    }

    @Override
    default String name() {
        return "";
    }

    @Override
    default String path() {
        return "";
    }
}
