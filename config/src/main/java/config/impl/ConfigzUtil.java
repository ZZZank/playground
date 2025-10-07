package config.impl;

import java.lang.reflect.*;

/**
 * @author ZZZank
 */
public class ConfigzUtil {

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType parameterizedType) {
            // getRawType() returns Type instead of Class; that seems to be an API mistake,
            // see https://bugs.openjdk.org/browse/JDK-8250659
            var rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            var componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();

        } else if (type instanceof TypeVariable<?> typeVariable) {
            // do not return Object.class directly, it's not how Java erase generic type info
            //            return Object.class;
            return getRawType(typeVariable.getBounds()[0]);

        } else if (type instanceof WildcardType) {
            var bounds = ((WildcardType) type).getUpperBounds();
            // Currently the JLS only permits one bound for wildcards so using first bound is safe
            return getRawType(bounds[0]);
        }
        var className = type == null ? "null" : type.getClass().getName();
        throw new IllegalArgumentException(
            String.format(
                "Expected a Class, ParameterizedType, or GenericArrayType, but <%s> is of type %s",
                type, className));
    }
}
