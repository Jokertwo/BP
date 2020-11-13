package my.bak.trafic.core.event;

import com.google.inject.Singleton;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.util.*;


@Singleton
public class EventBus implements Bus {

    @InjectLogger
    private static Logger logger;

    private Map<EventType, List<EventHandler>> listeners;


    public EventBus() {
        listeners = Collections.synchronizedMap(new HashMap<>());
    }


    @Override
    public void registerEventHandler(EventType eventType, EventHandler listener) {
        List<EventHandler> handlers = listeners.computeIfAbsent(eventType, v -> new ArrayList<>());
        if (!handlers.contains(listener)) {
            handlers.add(listener);
        } else {
            logger.warn("Cannot add two same handlers");
        }

    }


    @Override
    public void unregisterEventHandler(EventType eventType, EventHandler listener) {
        List<EventHandler> handler = listeners.getOrDefault(eventType, Collections.emptyList());
        handler.remove(listener);
    }


    @Override
    public void publishEvent(Event event) {
        List<EventHandler> handler = listeners.getOrDefault(event.getEventType(), Collections.emptyList());
        handler.forEach(listener -> listener.handleEvent(event));
    }

}
