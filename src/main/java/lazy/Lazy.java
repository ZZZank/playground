package lazy;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public final class Lazy<T> implements Supplier<T> {
    public static <T> Supplier<T> of(Supplier<T> supplier) {
        if (supplier instanceof Lazy<T> lazy) {
            return lazy;
        }
        return new Lazy<>(supplier);
    }

    public static <T> Supplier<T> serviceLoader(Class<T> type) {
        return of(() -> ServiceLoader.load(type)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No implementation found for " + type.getName())));
    }

    public static <T> Supplier<T> serviceLoader(Class<T> type, T fallback) {
        return of(() -> ServiceLoader.load(type)
            .findFirst()
            .orElse(fallback));
    }

    private Lazy(Supplier<T> factory) {
        this.factoryOrInstance = Objects.requireNonNull(factory);
        this.initialized = false;
    }

    private Object factoryOrInstance;
    private volatile boolean initialized;

    @Override
    public T get() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    this.factoryOrInstance = ((Supplier<T>) factoryOrInstance).get();
                    this.initialized = true;
                }
            }
        }
        return (T) factoryOrInstance;
    }
}
