package caller;

import lombok.val;

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
                    GETTER = findGetter(errorReporter);
                }
            }
        }
        return GETTER;
    }

    public static Supplier<Class<?>> of() {
        return of(null);
    }

    private static Supplier<Class<?>> findGetter(Consumer<Exception> errorReporter) {
        if (errorReporter == null) {
            errorReporter = (ignored) -> {};
        }

        // modern
        try {
            val class$StackWalker = Class.forName("java.lang.StackWalker");
            val class$StackWalker$Option = Class.forName("java.lang.StackWalker$Option");
            val lookup = MethodHandles.lookup();

            val instance$StackWalker = class$StackWalker
                .getMethod("getInstance", class$StackWalker$Option)
                .invoke(null, class$StackWalker$Option.getField("RETAIN_CLASS_REFERENCE").get(null));
            val handle = lookup.unreflect(class$StackWalker.getMethod("getCallerClass"))
                .bindTo(instance$StackWalker);
            return () -> {
                try {
                    return (Class<?>) handle.invokeExact();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (Exception ex) {
            errorReporter.accept(ex);
            // fall through
        }

        // legacy
        try {
            val class$Reflection = Class.forName("sun.reflect.Reflection");
            val lookup = MethodHandles.lookup();

            val handle = lookup.unreflect(class$Reflection.getMethod("getCallerClass", int.class));
            return () -> {
                try {
                    //0: Reflection
                    //1: CallerClassGetter
                    //2: this lambda
                    //3: caller class <-
                    return (Class<?>) handle.invokeExact(3);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (Exception ex) {
            errorReporter.accept(ex);
            // fall through
        }

        // fail
        throw new IllegalStateException("Unable to find caller class getter");
    }
}
