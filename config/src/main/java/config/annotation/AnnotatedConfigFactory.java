package config.annotation;

import config.ConfigCategory;
import config.ConfigIO;
import config.ConfigRoot;
import config.binding.FieldBinding;
import config.impl.ConfigzUtil;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public class AnnotatedConfigFactory {
    public static Supplier<ConfigRoot> create(Class<?> target, ConfigIO io, Path saveTo) {
        return new AnnotatedConfigFactory(target).buildSupplier(io, saveTo);
    }

    private final Class<?> target;
    private final Config rootAnnotation;

    private AnnotatedConfigFactory(Class<?> target) {
        this.target = target;
        this.rootAnnotation = Objects.requireNonNull(target.getAnnotation(Config.class));
    }

    public Supplier<ConfigRoot> buildSupplier(ConfigIO io, Path saveTo) {
        return () -> {
            var root = ConfigRoot.of(io, saveTo);
            fillConfigValue(target, root);
            processSubConfigs(target, root);
            return root;
        };
    }

    protected void fillConfigValue(Class<?> target, ConfigCategory category) {
        for (var field : target.getDeclaredFields()) {
            var modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)
                || !Modifier.isStatic(modifiers)
                || Modifier.isFinal(modifiers)
            ) {
                continue;
            }

            var name = readEntryInfo(field, Config.Entry::name)
                .filter(ConfigzUtil::notEmpty)
                .orElseGet(field::getName);
            var builder = category.define(name);
            try {
                var binding = new FieldBinding<>(field, null, name);
                builder.bind(binding);
                var type = field.getGenericType();
                if (type instanceof Class<?> c) {
                    type = ConfigzUtil.toBoxedType(c);
                }
                builder.setDefaultType(type);
            } catch (Exception e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }

            readEntryInfo(field, Config.Entry::comments)
                .ifPresent(builder::comment);

            builder.build();
        }
    }
    
    private void processSubConfigs(Class<?> target, ConfigCategory parent) {
        var subConfigClasses = new HashSet<Class<?>>();

        var configAnnotation = target.getAnnotation(Config.class);
        if (configAnnotation != null) {
            subConfigClasses.addAll(Arrays.asList(configAnnotation.subConfigs()));
            if (configAnnotation.innerClassAsSubConfig()) {
                subConfigClasses.addAll(Arrays.asList(target.getClasses()));
            }
        } else if (rootAnnotation.innerClassAsSubConfig()) {
            subConfigClasses.addAll(Arrays.asList(target.getClasses()));
        }

        for (var subConfig : subConfigClasses) {
            var name = readEntryInfo(subConfig, Config.Entry::name)
                .filter(ConfigzUtil::notEmpty)
                .orElseGet(subConfig::getSimpleName);
            var subCategory = parent.subCategory(name);
            fillConfigValue(subConfig, subCategory);
            processSubConfigs(subConfig, subCategory);
        }
    }

    private static <T> Optional<T> readEntryInfo(AnnotatedElement annotated, Function<Config.Entry, T> reader) {
        var anno = annotated.getAnnotation(Config.Entry.class);
        if (anno != null) {
            return Optional.of(reader.apply(anno));
        }
        return Optional.empty();
    }
}