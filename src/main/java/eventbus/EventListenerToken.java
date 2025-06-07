package eventbus;

/**
 * @author ZZZank
 */
public interface EventListenerToken<E extends Event> {
    Class<E> eventType();

    byte priority();
}
