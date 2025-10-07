package config.impl.struct;

import config.ConfigCategory;
import config.ConfigEntry;
import config.impl.ConfigzUtil;
import lombok.Getter;
import lombok.experimental.Accessors;
import utils.Asser;
import config.ConfigBinding;
import config.prop.ConfigProperties;

import java.lang.reflect.Type;

/**
 * @author ZZZank
 */
@Getter
@Accessors(fluent = true, makeFinal = true)
class ConfigEntryImpl<T> implements ConfigEntry<T> {
    private final String name;
    private final ConfigBinding<T> binding;
    private final ConfigProperties properties;
    private final ConfigCategory parent;
    Type defaultType;

    public ConfigEntryImpl(
        String name,
        ConfigBinding<T> binding,
        ConfigProperties properties,
        ConfigCategory parent,
        Type defaultType
    ) {
        this.name = Asser.tNotNull(name, "name");
        this.binding = Asser.tNotNull(binding, "binding");
        this.properties = Asser.tNotNull(properties, "properties");
        this.parent = parent;
        this.defaultType = defaultType;

        if (!this.isRoot()) {
            Asser.t(!name.isEmpty(), "non-root config entry should have not-empty name");
            Asser.tNotNull(parent, "parent for non-root config entry");
        }
        Asser.t(
            name.indexOf(ConfigCategory.PATH_SPLITTER_CHAR) < 0,
            String.format(
                "There should not be path splitter (%s) in config entry name",
                ConfigCategory.PATH_SPLITTER_CHAR
            )
        );
        Asser.t(
            ConfigzUtil.getRawType(this.defaultType()).isInstance(binding.getDefault()),
            "config default value must match expected type"
        );
    }
}
