package config.impl.struct;

import config.ConfigEntry;
import config.ConfigRoot;
import lombok.Getter;
import lombok.experimental.Accessors;
import utils.Asser;
import config.ConfigIO;
import config.prop.ConfigProperties;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
@Getter
@Accessors(fluent = true, makeFinal = true)
public class ConfigRootImpl extends ConfigCategoryImpl implements ConfigRoot {
    private final ConfigIO io;
    private final Path filePath;

    public ConfigRootImpl(ConfigIO io, Path path) {
        this(LinkedHashMap::new, new ConfigProperties(), io, path);
    }

    public ConfigRootImpl(
        Supplier<Map<String, ConfigEntry<?>>> provider,
        ConfigProperties properties,
        ConfigIO io,
        Path path
    ) {
        super("", provider, properties, null);
        this.io = Asser.tNotNull(io, "config io");
        this.filePath = path;
    }

    @Override
    public void fillEntryType(Class<?> declaringClass) {
        for (var field : declaringClass.getDeclaredFields()) {
            var modifiers = field.getModifiers();
            if (
                Modifier.isPublic(modifiers)
                && Modifier.isStatic(modifiers)
                && Modifier.isFinal(modifiers)
                && ConfigEntry.class.isAssignableFrom(field.getType())
                && field.getGenericType() instanceof ParameterizedType parameterized
            ) {
                // public static final ConfigEntry<XXX> someField
                var param = parameterized.getActualTypeArguments()[0];
                try {
                    var o = (ConfigEntryImpl<?>) field.get(null);
                    if (o.getRoot() == this) {
                        o.setDefaultType(param);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access config entry from field: " + field.getName(), e);
                }
            }
        }
    }
}
