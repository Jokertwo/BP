package my.bak.trafic.core.event.types;

import lombok.Getter;
import my.bak.trafic.core.event.Event;
import my.bak.trafic.core.event.EventType;


public class StatusChangeEvent implements Event {

    @Getter
    private String status;
    @Getter
    private String description;


    public StatusChangeEvent(String status, String description) {
        super();
        this.status = status;
        this.description = description;
    }


    @Override
    public EventType getEventType() {
        return EventType.STATUS;
    }

}
