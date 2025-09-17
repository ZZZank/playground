package test.caller;

import caller.CallerClassGetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class CallerClassGetterTest {

    @Test
    public void test() throws Throwable {
        var callerClass = CallerClassGetter.of(System.err::println)
            .get();
        Assertions.assertEquals(CallerClassGetterTest.class, callerClass);
    }

    @Test
    public void innerClassInstance() {
        var inner = new Inner();
        Assertions.assertEquals(Inner.class, inner.get());
    }

    @Test
    public void innerClassStatic() {
        Assertions.assertEquals(Inner.class, Inner.getStatic());
    }

    private static class Inner {

        public Class<?> get() {
            return CallerClassGetter.of(System.err::println).get();
        }

        public static Class<?> getStatic() {
            return CallerClassGetter.of(System.err::println).get();
        }
    }
}
