package my.bak.trafic.core.event;

public interface Bus {

    /**
     * Register new event handler
     *
     * @param eventType type of event
     * @param listener  {@link EventHandler}
     */
    void registerEventHandler(EventType eventType, EventHandler listener);


    /**
     * Unregister current event handler
     *
     * @param eventType type of event
     * @param listener  {@link EventHandler}
     */
    void unregisterEventHandler(EventType eventType, EventHandler listener);


    /**
     * Propagate event to handlers
     *
     * @param event {@link Event} IEvent, should be propagated
     */
    void publishEvent(Event event);
}
