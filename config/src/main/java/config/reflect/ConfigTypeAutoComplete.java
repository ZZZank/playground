package config.reflect;

import config.ConfigEntry;
import config.impl.struct.ConfigEntryImpl;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author ZZZank
 */
public class ConfigTypeAutoComplete {

    /// Fill config value types for all entries in `target` and its member classes.
    ///
    /// @see #forEntriesIn(Class, boolean)
    public static void forEntriesIn(Class<?> target) {
        forEntriesIn(target, true);
    }

    /// Fill config value types for all entries in `target`, and optionally its member classes.
    ///
    /// @param includeMemberClasses If `true`, this process will also be applied to the `target`'s member classes, recursively
    public static void forEntriesIn(Class<?> target, boolean includeMemberClasses) {
        if (includeMemberClasses) {
            classAndMemberClasses(target).forEach(ConfigTypeAutoComplete::forEntriesInSingle);
        } else {
            forEntriesInSingle(target);
        }
    }

    private static Stream<Class<?>> classAndMemberClasses(Class<?> start) {
        var memberClasses = start.getClasses();
        if (memberClasses.length == 0) {
            return Stream.of(start);
        }
        return Stream.concat(
            Stream.of(start),
            Arrays.stream(memberClasses)
                .flatMap(ConfigTypeAutoComplete::classAndMemberClasses)
        );
    }

    private static void forEntriesInSingle(Class<?> target) {
        for (var field : target.getDeclaredFields()) {
            var modifiers = field.getModifiers();
            if (
                Modifier.isPublic(modifiers)
                && Modifier.isStatic(modifiers)
                && Modifier.isFinal(modifiers)
                && ConfigEntry.class == field.getType()
                && field.getGenericType() instanceof ParameterizedType parameterized
            ) {
                // public static final ConfigEntry<XXX> someField
                var param = parameterized.getActualTypeArguments()[0];
                try {
                    var o = (ConfigEntryImpl<?>) field.get(null);
                    o.setDefaultType(param);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access config entry from field: " + field.getName(), e);
                }
            }
        }
    }
}
