package eventbus;

import eventbus.dispatch.DispatchCancellableEventBus;
import eventbus.dispatch.DispatchEventBus;
import eventbus.dispatch.DispatchKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZZZank
 */
public class EventBusTest {
    private static final EventBus<List<String>> BUS =
        EventBus.create(List.class).cast();
    private static final CancellableEventBus<List<String>> BUS_CANCELLABLE =
        CancellableEventBus.create(List.class).cast();
    private static final DispatchEventBus<List<String>, String> BUS_DISPATCH =
        DispatchEventBus.create(List.class, DispatchKey.create(String.class)).castDispatch();
    private static final DispatchCancellableEventBus<List<String>, String> BUS_DISPATCH_CANCELLABLE =
        DispatchCancellableEventBus.create(List.class, DispatchKey.create(String.class)).castDispatch();

    @Test
    public void testRegular() {
        testRegularBus(BUS);
        testRegularBus(BUS_CANCELLABLE);
        testRegularBus(BUS_DISPATCH);
        testRegularBus(BUS_DISPATCH_CANCELLABLE);
    }

    private static void testRegularBus(EventBus<List<String>> bus) {
        var token0 = bus.addListener(CommonPriority.HIGH, l -> l.add("0"));
        var token1 = bus.addListener(l -> l.add("1"));
        var token2 = bus.addListener(CommonPriority.LOW, l -> l.add("2"));

        testToken(bus, token0, CommonPriority.HIGH);
        testToken(bus, token1, CommonPriority.NORMAL);
        testToken(bus, token2, CommonPriority.LOW);

        ArrayList<String> toTest;

        bus.post(toTest = new ArrayList<>());
        Assertions.assertEquals(toTest, Arrays.asList("0", "1", "2"));

        bus.unregister(token1);
        bus.post(toTest = new ArrayList<>());
        Assertions.assertEquals(toTest, Arrays.asList("0", "2"));

        Assertions.assertTrue(bus.unregister(token0));
        Assertions.assertFalse(bus.unregister(token1)); // unregistered before
        Assertions.assertTrue(bus.unregister(token2));
    }

    private static <E> void testToken(EventBus<E> bus, EventListenerToken<E> token, byte expectedPriority) {
        Assertions.assertEquals(expectedPriority, token.priority());
        Assertions.assertSame(bus.eventType(), token.eventType());
    }
}
