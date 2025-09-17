package eventbus;

/**
 * @author ZZZank
 */
public interface EventListenerToken<E> {
    Class<E> eventType();

    byte priority();
}
