package config.io;

import org.jetbrains.annotations.NotNull;
import zzzank.probejs.utils.Cast;
import zzzank.probejs.utils.CollectUtils;
import zzzank.probejs.utils.config.prop.ConfigProperty;
import zzzank.probejs.utils.config.serde.ConfigSerde;
import zzzank.probejs.utils.config.struct.ConfigEntry;
import zzzank.probejs.utils.config.struct.ConfigRoot;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author ZZZank
 */
public class PropertiesConfigIO extends SerdeHolder<String> implements ConfigIO {

    public static final ConfigProperty<ConfigSerde<String, ?>> PROP_SERDE =
        ConfigProperty.register("properties_io_serde", null);

    public PropertiesConfigIO() {
        super(PROP_SERDE);
        addPrimitiveSerde(Byte::valueOf, Byte.class, byte.class);
        addPrimitiveSerde(Short::valueOf, Short.class, short.class);
        addPrimitiveSerde(Integer::valueOf, Integer.class, int.class);
        addPrimitiveSerde(Long::valueOf, Long.class, long.class);
        addPrimitiveSerde(Float::valueOf, Float.class, float.class);
        addPrimitiveSerde(Double::valueOf, Double.class, double.class);
        addPrimitiveSerde(s -> s.charAt(0), Character.class, char.class);
        addPrimitiveSerde(Boolean::valueOf, Boolean.class, boolean.class);
    }

    @Override
    public void read(ConfigRoot config, Reader reader) throws IOException {
        var properties = new Properties();
        properties.load(reader);
        for (var entry : properties.entrySet()) {
            var path = (String) entry.getKey();
            var value = (String) entry.getValue();

            var configEntry = config.getEntry(path);
            if (configEntry == null) {
                continue;
            }

            var serde = getSerde(configEntry);

            configEntry.set(Cast.to(serde.deserialize(value)));
        }
    }

    @Override
    public void save(ConfigRoot config, Writer writer) throws IOException {
        var properties = new Properties();
        var comments = new ArrayList<String>();
        for (var entry : CollectUtils.iterate(walkEntries(config))) {
            var serde = getSerde(entry);

            var defaultValue = entry.getDefault();
            var currentValue = entry.get();

            comments.add("");
            comments.add(String.format("%s -> %s (default)", entry.path(), serde.serialize(Cast.to(defaultValue))));
            entry.getProp(ConfigProperty.COMMENTS)
                .orElse(Collections.emptyList())
                .stream()
                .map(s -> '\t' + s)
                .forEach(comments::add);

            if (!defaultValue.equals(currentValue)) {
                properties.setProperty(entry.path(), serde.serialize(Cast.to(currentValue)));
            }
        }
        properties.store(writer, String.join("\n", comments));
    }

    @SafeVarargs
    private <T> void addPrimitiveSerde(StringRepresentativeSerde<T> serde, Class<T>... types) {
        var toMatch = Set.of(types);
        registerSerdeFactory(t -> t instanceof Class<?> c && toMatch.contains(c) ? serde : null);
    }

    private static Stream<ConfigEntry<?>> walkEntries(ConfigEntry<?> entry) {
        if (entry.isCategory()) {
            return entry.asCategory()
                .get()
                .values()
                .stream()
                .flatMap(PropertiesConfigIO::walkEntries);
        }
        return Stream.of(entry);
    }

    @FunctionalInterface
    interface StringRepresentativeSerde<T> extends ConfigSerde<String, T> {
        @Override
        @NotNull
        default String serialize(@NotNull T value) {
            return value.toString();
        }
    }
}
