package my.bak.trafic.core.event;

/**
 * Handler which can handle events form EventBus
 *
 * @author Petr Lastovka
 */
public interface EventHandler {

    /**
     * Handle of current event
     *
     * @param event
     */
    void handleEvent(Event event);
}
