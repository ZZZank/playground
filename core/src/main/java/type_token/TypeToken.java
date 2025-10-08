package type_token;

import java.lang.reflect.*;

/**
 * @author ZZZank
 */
public abstract class TypeToken<T> {

    public static TypeToken<?> wrap(Type type) {
        return new TypeToken<>(type) {
        };
    }

    public static <T> TypeToken<T> wrap(Class<T> type) {
        return new TypeToken<>(type) {
        };
    }

    private final Type inner;

    protected TypeToken(Type inner) {
        this.inner = inner;
    }

    public TypeToken() {
        this.inner = extractType();
    }

    private Type extractType() {
        var clazz = this.getClass();
        if (!clazz.isAnonymousClass()) {
            throw new IllegalStateException("Type auto extraction can only be applied to an anonymous class");
        }
        var fullType = clazz.getGenericSuperclass();
        if (!(fullType instanceof ParameterizedType parameterized)) {
            throw new IllegalStateException(
                "Type auto extraction can only be applied to generic implementation of TypeToken");
        }
        return parameterized.getActualTypeArguments()[0];
    }

    public Type unwrap() {
        return inner;
    }

    public Class<T> asClass() {
        return (Class<T>) toClass(this.inner);
    }

    private static Class<?> toClass(Type type) {
        if (type instanceof Class<?> c) {
            return c;
        } else if (type instanceof TypeVariable<?> variable) {
            return toClass(variable.getBounds()[0]);
        } else if (type instanceof ParameterizedType parameterized) {
            return toClass(parameterized.getRawType());
        } else if (type instanceof GenericArrayType arrayType) {
            return Array.newInstance(toClass(arrayType.getGenericComponentType()), 0).getClass();
        } else if (type instanceof WildcardType wildcard) {
            var lower = wildcard.getLowerBounds();
            if (lower.length != 0) {
                return toClass(lower[0]);
            }
            return toClass(wildcard.getUpperBounds()[0]);
        }
        throw new IllegalArgumentException("Unknown implementation of Type: " + type);
    }
}
