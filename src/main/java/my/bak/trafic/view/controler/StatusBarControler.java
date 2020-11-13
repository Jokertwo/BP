package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.Event;
import my.bak.trafic.core.event.EventHandler;
import my.bak.trafic.core.event.EventType;
import my.bak.trafic.core.event.types.ConnectionEvent;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


@Singleton
public class StatusBarControler implements EventHandler, Initializable {

    @InjectLogger
    private static Logger logger;

    @FXML
    private Label status;

    @FXML
    private Label connected;

    private ThreadPool pool;

    private Image disconectedIcon;
    private Image connectedIcon;


    @Inject
    public StatusBarControler(Bus bus, ThreadPool pool) {
        this.pool = pool;
        bus.registerEventHandler(EventType.STATUS, this);
        bus.registerEventHandler(EventType.CONNECTION_STATUS, this);
        disconectedIcon = new Image("icons/error.jpg", 20, 20, true, true, false);
        connectedIcon = new Image("icons/ok.jpg", 30, 30, true, true, false);
    }


    @Override
    public void handleEvent(Event event) {
        if (event instanceof StatusChangeEvent) {
            pool.getFXExecutor().execute(() -> {
                String newStatus = ((StatusChangeEvent) event).getStatus();
                String toolTip = ((StatusChangeEvent) event).getDescription();
                logger.info("Changing status: old: {}, new: {}, description: {}", status.getText(), newStatus, toolTip);
                status.setTooltip(new Tooltip(toolTip));
                status.setText(newStatus);
            });
        }
        if (event instanceof ConnectionEvent) {
            pool.getFXExecutor().execute(() -> setNewConnectionIcon(((ConnectionEvent) event).isConnected()));
        }
    }


    private void setNewConnectionIcon(boolean isConnected) {
        ImageView view;
        Tooltip tooltip = new Tooltip();
        if (isConnected) {
            view = new ImageView(connectedIcon);
            tooltip.setText("You are connected to DB");
        } else {
            view = new ImageView(disconectedIcon);
            tooltip.setText("You are not connected to DB");
        }
        connected.setGraphic(view);
        connected.setTooltip(tooltip);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNewConnectionIcon(false);
    }

}
