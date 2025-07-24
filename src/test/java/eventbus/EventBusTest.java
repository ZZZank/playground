package eventbus;

import eventbus.dispatch.DispatchCancellableEventBus;
import eventbus.dispatch.DispatchEventBus;
import eventbus.dispatch.DispatchKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public class EventBusTest {
    private static final EventBus<Set<String>> BUS =
        EventBus.create(Set.class).cast();
    private static final CancellableEventBus<Set<String>> BUS_CANCELLABLE =
        CancellableEventBus.create(Set.class).cast();
    private static final DispatchEventBus<Set<String>, String> BUS_DISPATCH =
        DispatchEventBus.create(Set.class, DispatchKey.create(String.class)).castDispatch();
    private static final DispatchCancellableEventBus<Set<String>, String> BUS_DISPATCH_CANCELLABLE =
        DispatchCancellableEventBus.create(Set.class, DispatchKey.create(String.class)).castDispatch();

    @Test
    public void testRegular() {
        testRegularBus(BUS);
        testRegularBus(BUS_CANCELLABLE);
        testRegularBus(BUS_DISPATCH);
        testRegularBus(BUS_DISPATCH_CANCELLABLE);
    }

    @Test
    public void testCancellable() {
        testCancellableBus(BUS_CANCELLABLE);
        testCancellableBus(BUS_DISPATCH_CANCELLABLE);
    }

    @Test
    public void testDispatch() {
        testDispatchBus(BUS_DISPATCH);
        testDispatchBus(BUS_DISPATCH_CANCELLABLE);
    }

    private static void testDispatchBus(DispatchEventBus<Set<String>, String> bus) {
        var token0 = bus.addListener(CommonPriority.HIGH, l -> l.add("0"));
        var token1 = bus.addListener("1", l -> l.add("1"));
        var token2 = bus.addListener("2", l -> l.add("2"));

        testToken(bus, token0, CommonPriority.HIGH);
        testToken(bus, token1, CommonPriority.NORMAL);
        testToken(bus, token2, CommonPriority.NORMAL);

        Set<String> toTest;

        bus.post(toTest = new HashSet<>());
        Assertions.assertEquals(Set.of("0"), toTest);

        bus.post(toTest = new HashSet<>(), "1");
        Assertions.assertEquals(Set.of("0", "1"), toTest);

        Assertions.assertTrue(bus.unregister(token1));
        bus.post(toTest = new HashSet<>(), "1");
        Assertions.assertEquals(Set.of("0"), toTest);

        Assertions.assertTrue(bus.unregister(token0));
        Assertions.assertFalse(bus.unregister(token1)); // unregistered before
        Assertions.assertTrue(bus.unregister(token2));
    }

    private static void testCancellableBus(CancellableEventBus<Set<String>> bus) {
        var token0 = bus.addListener(
            CommonPriority.HIGH,
            (Consumer<Set<String>>) l -> l.add("0")
        );
        var token1 = bus.addListener(
            (Predicate<Set<String>>) l -> l.add("1")
        );
        var token2 = bus.addListener(
            CommonPriority.LOW,
            (Consumer<Set<String>>) l -> l.add("2")
        );

        testToken(bus, token0, CommonPriority.HIGH);
        testToken(bus, token1, CommonPriority.NORMAL);
        testToken(bus, token2, CommonPriority.LOW);

        Set<String> toTest;

        Assertions.assertTrue(bus.post(toTest = new HashSet<>()), "event should be cancelled");
        Assertions.assertEquals(Set.of("0", "1"), toTest); // "2" is not invoked due to the event being cancelled

        Assertions.assertTrue(bus.unregister(token1));
        Assertions.assertFalse(bus.post(toTest = new HashSet<>()), "event should not be cancelled");
        Assertions.assertEquals(Set.of("0", "2"), toTest);

        Assertions.assertTrue(bus.unregister(token0));
        Assertions.assertFalse(bus.unregister(token1)); // unregistered before
        Assertions.assertTrue(bus.unregister(token2));
    }

    private static void testRegularBus(EventBus<Set<String>> bus) {
        var token0 = bus.addListener(CommonPriority.HIGH, l -> l.add("0"));
        var token1 = bus.addListener(l -> l.add("1"));
        var token2 = bus.addListener(CommonPriority.LOW, l -> l.add("2"));

        testToken(bus, token0, CommonPriority.HIGH);
        testToken(bus, token1, CommonPriority.NORMAL);
        testToken(bus, token2, CommonPriority.LOW);

        Set<String> toTest;

        bus.post(toTest = new HashSet<>());
        Assertions.assertEquals(Set.of("0", "1", "2"), toTest);

        Assertions.assertTrue(bus.unregister(token1));
        bus.post(toTest = new HashSet<>());
        Assertions.assertEquals(Set.of("0", "2"), toTest);

        Assertions.assertTrue(bus.unregister(token0));
        Assertions.assertFalse(bus.unregister(token1)); // unregistered before
        Assertions.assertTrue(bus.unregister(token2));
    }

    private static <E> void testToken(EventBus<E> bus, EventListenerToken<E> token, byte expectedPriority) {
        Assertions.assertEquals(expectedPriority, token.priority());
        Assertions.assertSame(bus.eventType(), token.eventType());
    }
}
