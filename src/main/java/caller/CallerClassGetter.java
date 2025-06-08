package caller;

import lombok.val;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public final class CallerClassGetter {
    private static volatile Supplier<Class<?>> GETTER;

    public static Supplier<Class<?>> of(Consumer<Exception> errorReporter) {
        if (GETTER == null) {
            synchronized (CallerClassGetter.class) {
                if (GETTER == null) {
                    val handle = findHandle(errorReporter);
                    GETTER = () -> {
                        try {
                            return (Class<?>) handle.invokeExact();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    };
                }
            }
        }
        return GETTER;
    }

    public static Supplier<Class<?>> of() {
        return of(null);
    }

    private static MethodHandle findHandle(Consumer<Exception> errorReporter) {
        if (errorReporter == null) {
            errorReporter = (ignored) -> {};
        }

        // modern
        try {
            val class$StackWalker = Class.forName("java.lang.StackWalker");
            val class$StackWalker$Option = Class.forName("java.lang.StackWalker$Option");
            val lookup = MethodHandles.lookup();

            val handle = lookup.unreflect(class$StackWalker.getMethod("getCallerClass"));
            val instance$StackWalker = class$StackWalker
                .getMethod("getInstance", class$StackWalker$Option)
                .invoke(null, class$StackWalker$Option.getField("RETAIN_CLASS_REFERENCE").get(null));
            return handle.bindTo(instance$StackWalker);
        } catch (Exception ex) {
            errorReporter.accept(ex);
            // fall through
        }

        // legacy
        try {
            val class$Reflection = Class.forName("sun.reflect.Reflection");
            val lookup = MethodHandles.lookup();

            return lookup.unreflect(class$Reflection.getMethod("getCallerClass"));
        } catch (Exception ex) {
            errorReporter.accept(ex);
            // fall through
        }

        // fail
        throw new IllegalStateException("Unable to find caller class getter");
    }
}
