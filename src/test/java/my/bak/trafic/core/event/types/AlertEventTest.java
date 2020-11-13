package my.bak.trafic.core.event.types;

import javafx.scene.control.Alert.AlertType;
import my.bak.trafic.core.event.EventType;
import my.bak.trafic.test.hepler.JfxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(JfxTestRunner.class)
public class AlertEventTest {
    private AlertEvent event;


    @Test
    public void testGetEventType() {
        event = new AlertEvent(null, null, null);
        assertEquals(EventType.ALERT, event.getEventType());
    }


    @Test
    public void testGetType() {
        event = new AlertEvent(AlertType.CONFIRMATION, null, null);
        assertEquals(AlertType.CONFIRMATION, event.getType());
    }


    @Test
    public void testGetTitleHeader() {
        event = new AlertEvent(null, "txt", null);
        assertEquals("txt", event.getTitleHeader());
    }


    @Test
    public void testGetBody() {
        String label = "ahoj";
        event = new AlertEvent(null, null, label);
        assertEquals(label, event.getContent());
    }


    @Test
    public void testGetException() {
        Exception e = new IllegalStateException();
        event = new AlertEvent(null, null, null, e);
        assertEquals(e, event.getException());
    }

}
