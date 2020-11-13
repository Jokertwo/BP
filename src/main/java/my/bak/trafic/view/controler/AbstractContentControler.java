package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import com.sun.org.apache.bcel.internal.util.ClassLoader;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lombok.Getter;
import my.bak.trafic.core.GuiceFXMLLoader;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.view.ControlerContants;
import my.bak.trafic.view.QueryController;
import my.bak.trafic.view.TabController;
import my.bak.trafic.view.clipboard.Clipboard;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;


public abstract class AbstractContentControler implements TabController, Initializable {

    private Logger logger;

    @FXML
    private BorderPane content;

    @FXML
    @Getter
    private BorderPane leftContent;

    @FXML
    @Getter
    private BorderPane rightContent;

    @FXML
    @Getter
    private Button closeBtn;

    @FXML
    @Getter
    private Button copyBtn;

    @FXML
    @Getter
    private Button pasteBtn;

    @FXML
    @Getter
    private Button runBtn;

    @FXML
    private SplitPane splitPane;

    private double exapanded = 0.31;
    private boolean isRunning = false;
    private BooleanProperty isCollapsed;
    private Clipboard clipboard;
    @Getter
    private QueryController queryControler;


    @Inject
    public AbstractContentControler(Logger logger, Clipboard clipboard) {
        this.logger = logger;
        this.clipboard = clipboard;
    }

    /**
     * action to collapse/open side panel
     *
     * @param event <b>parameter is not used</b>
     */
    @FXML
    void handleCloseBtn(ActionEvent event) {
        if (!isRunning) {
            isRunning = true;
            exapanded = isCollapsed.get() ? exapanded : splitPane.getDividers().get(0).getPosition();
            double target = isCollapsed.get() ? exapanded : 0.0;
            KeyValue keyValue = new KeyValue(splitPane.getDividers().get(0).positionProperty(), target);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), keyValue));
            timeline.onFinishedProperty().set(this::stopAnimate);
            timeline.play();
        }
    }


    @FXML
    public void handleCopyBtn(ActionEvent event) {
        logger.info("Handle copy btn");
        clipboard.store(queryControler.getColumns(), queryControler.getRoot(), queryControler.isDistinct());
    }


    /**
     * Execute specific action by panel
     */
    @FXML
    abstract void handleRunBtn(ActionEvent event);

    /**
     * Paste values for clipboard
     *
     * @param event <b>parameter is not used</b>
     */
    @FXML
    public void handlePasteBtn(ActionEvent event) {
        logger.info("Handle paste btn");
        queryControler.getColumns().clear();
        queryControler.getRoot().getChildren().clear();

        queryControler.getColumns().addAll(clipboard.getColumns());
        queryControler.getRoot().getChildren().addAll(clipboard.getRoot().getChildren());
        queryControler.setDistinct(clipboard.getDistinct());
    }


    @Override
    public Node getContent() {
        return content;
    }


    @Override
    public void setDisable(boolean disable) {
        content.setDisable(disable);
    }


    /**
     * Load new component for creating queries
     *
     * @return return controller for query panel
     */
    private QueryController loadQueryController() {
        logger.info("Loading query component and controller");
        try (InputStream input = ClassLoader.getSystemResourceAsStream(ControlerContants.QUERY_BUILDER_VIEW)) {
            GuiceFXMLLoader loader = GuiceFXMLLoader.getInstance();
            loader.load(input);
            return loader.getController();
        } catch (IOException e) {
            logger.fatal("Query component can not be loaded", e);
            return null;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // collapse property
        isCollapsed = new SimpleBooleanProperty();
        isCollapsed.bind(splitPane.getDividers().get(0).positionProperty().isEqualTo(0, 0.01));

        queryControler = loadQueryController();
        getLeftContent().setCenter(queryControler.getContent());
        queryControler.getColumns().addListener(this::disableExecuteBtn);

        pasteBtn.disableProperty().bind(clipboard.isEmpty());
    }


    private void disableExecuteBtn(Change<? extends Column> c) {
        getRunBtn().setDisable(c.getList().isEmpty());
    }


    /**
     * Set flag to stop animation
     *
     * @param event <b>parameter is not used</b>
     */
    private void stopAnimate(ActionEvent event) {
        isRunning = false;
    }

}
