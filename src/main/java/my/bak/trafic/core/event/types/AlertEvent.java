package my.bak.trafic.core.event.types;

import javafx.scene.control.Alert.AlertType;
import lombok.Getter;
import my.bak.trafic.core.event.Event;
import my.bak.trafic.core.event.EventType;


/**
 * Alert event with some information for s user, which should be shown as popup
 *
 * @author Petr Lastovka
 */
public class AlertEvent implements Event {

    @Getter
    private AlertType type;
    @Getter
    private String titleHeader;
    @Getter
    private Exception exception;
    @Getter
    private String content;


    public AlertEvent(AlertType type, String titleHeader, String content) {
        this(type, titleHeader, content, null);
    }


    public AlertEvent(AlertType type, String titleHeader, String content, Exception exception) {
        this.type = type;
        this.titleHeader = titleHeader;
        this.exception = exception;
        this.content = content;
    }


    @Override
    public EventType getEventType() {
        return EventType.ALERT;
    }

}
