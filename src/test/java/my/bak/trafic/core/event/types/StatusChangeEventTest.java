package my.bak.trafic.core.event.types;

import my.bak.trafic.core.event.EventType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class StatusChangeEventTest {
    private StatusChangeEvent event;


    @Test
    public void testGetEventType() {
        event = new StatusChangeEvent(null, null);
        assertEquals(EventType.STATUS, event.getEventType());
    }


    @Test
    public void testGetStatus() {
        event = new StatusChangeEvent("txt", null);
        assertEquals("txt", event.getStatus());
    }


    @Test
    public void testGetDescription() {
        event = new StatusChangeEvent(null, "txt");
        assertEquals("txt", event.getDescription());
    }

}
