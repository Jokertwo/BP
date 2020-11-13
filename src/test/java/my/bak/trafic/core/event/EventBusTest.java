package my.bak.trafic.core.event;

import my.bak.trafic.core.event.types.ConnectionEvent;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventBusTest {
    private Bus bus;
    private boolean isConnected;
    private EventHandler handler;

    @BeforeClass
    public static void tearUp() {
        LoggerTestInjector.inject(EventBus.class);
    }

    @Before
    public void setUp() throws Exception {
        bus = new EventBus();
        isConnected = false;
        handler = event -> isConnected = ((ConnectionEvent) event).isConnected();
    }


    @Test
    public void registerEventHandler() {
        bus.registerEventHandler(EventType.CONNECTION_STATUS, handler);
        bus.publishEvent(new ConnectionEvent(true));
        assertEquals(true, isConnected);
    }

    @Test
    public void unregisterEventHandler() {
        bus.registerEventHandler(EventType.CONNECTION_STATUS, handler);
        bus.publishEvent(new ConnectionEvent(true));
        assertEquals(true, isConnected);
        bus.unregisterEventHandler(EventType.CONNECTION_STATUS, handler);
        bus.publishEvent(new ConnectionEvent(false));
        assertEquals(true, isConnected);
    }

}