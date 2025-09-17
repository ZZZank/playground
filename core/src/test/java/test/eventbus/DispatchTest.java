package test.eventbus;

import eventbus.dispatch.DispatchEventBus;
import eventbus.dispatch.DispatchKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * @author ZZZank
 */
public class DispatchTest {
    private static final DispatchEventBus<IntConsumer, String> BUS = DispatchEventBus.create(
        IntConsumer.class,
        DispatchKey.create(String.class)
    );
    private static final DispatchEventBus<IntConsumer, String> BUS_AUTO_KEY = DispatchEventBus.create(
        IntConsumer.class,
        DispatchKey.create(
            String.class,
            e -> e instanceof IntConsumerTestImpl impl ? impl.key : null
        )
    );

    private static void fillListener(DispatchEventBus<IntConsumer, String> bus) {
        bus.addListener("1", e -> e.accept(1));
        bus.addListener("2", e -> e.accept(2));
        bus.addListener(e -> e.accept(-1));
    }

    static {
        fillListener(BUS);
        fillListener(BUS_AUTO_KEY);
    }

    @Test
    public void testNoKey() {
        var testImpl = new IntConsumerTestImpl();
        BUS.post(testImpl);
        testImpl.assertMatch(-1);
    }

    @Test
    public void testKey1() {
        var testImpl = new IntConsumerTestImpl();
        BUS.post(testImpl, "1");
        testImpl.assertMatch(-1, 1);
    }

    @Test
    public void testKey2() {
        var testImpl = new IntConsumerTestImpl();
        BUS.post(testImpl, "2");
        testImpl.assertMatch(-1, 2);
    }

    @Test
    public void testNoKeyAuto() {
        var testImpl = new IntConsumerTestImpl();
        BUS_AUTO_KEY.post(testImpl);
        testImpl.assertMatch(-1);
    }

    @Test
    public void testKey1Auto() {
        var testImpl = new IntConsumerTestImpl("1");
        BUS_AUTO_KEY.post(testImpl);
        testImpl.assertMatch(-1, 1);
    }

    private static class IntConsumerTestImpl implements IntConsumer {
        private final List<Integer> list = new ArrayList<>();
        private final String key;

        private IntConsumerTestImpl(String key) {
            this.key = key;
        }

        private IntConsumerTestImpl() {
            this.key = null;
        }

        @Override
        public void accept(int value) {
            list.add(value);
        }

        public void assertMatch(int... toMatch) {
            list.sort(null);
            var listToMatch = Arrays.stream(toMatch)
                .sorted()
                .boxed()
                .toList();
            Assertions.assertEquals(listToMatch, this.list);
        }
    }
}
