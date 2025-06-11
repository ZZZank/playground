package config.io;

import zzzank.probejs.utils.config.struct.ConfigRoot;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author ZZZank
 */
public interface ConfigIO {

    void read(ConfigRoot config, Reader reader) throws IOException;

    void save(ConfigRoot config, Writer writer) throws IOException;

    default void read(ConfigRoot config, Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path)) {
            read(config, reader);
        }
    }

    default void save(ConfigRoot config, Path path) throws IOException {
        try (var writer = Files.newBufferedWriter(path)) {
            save(config, writer);
        }
    }
}
