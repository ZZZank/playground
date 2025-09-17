package config.annotation;

import config.ConfigCategory;
import config.ConfigIO;
import config.ConfigRoot;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * @author ZZZank
 */
public class AnnotatedConfigFactory {
    public static final AnnotatedConfigFactory INSTANCE = new AnnotatedConfigFactory();

    public ConfigRoot create(Class<?> target, ConfigIO io, @Nullable Path saveTo) {
        var root = ConfigRoot.of(io, saveTo);
        return root;
    }

    protected void fillConfigValue(Class<?> target, ConfigCategory category) {
        target.getDeclaringClass();
    }
}
