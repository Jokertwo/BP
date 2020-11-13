package my.bak.trafic.core.event.types;

import my.bak.trafic.core.event.Event;
import my.bak.trafic.core.event.EventType;


/**
 * The event, which informs about that connection state to the database
 *
 * @author Petr Lastovka
 */
public class ConnectionEvent implements Event {

    private boolean isConnected = false;


    public ConnectionEvent(boolean isConnected) {
        super();
        this.isConnected = isConnected;
    }


    public boolean isConnected() {
        return isConnected;
    }


    @Override
    public EventType getEventType() {
        return EventType.CONNECTION_STATUS;
    }

}
