package my.bak.trafic.core.event.types;

import my.bak.trafic.core.event.EventType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ConnectionEventTest {

    private ConnectionEvent event;


    @Test
    public void testIsNotConnected() {
        event = new ConnectionEvent(false);
        assertEquals(false, event.isConnected());

    }


    @Test
    public void testIsConnected() {
        event = new ConnectionEvent(true);
        assertEquals(true, event.isConnected());

    }


    @Test
    public void testGetEventType() {
        event = new ConnectionEvent(false);
        assertEquals(EventType.CONNECTION_STATUS, event.getEventType());
    }

}
