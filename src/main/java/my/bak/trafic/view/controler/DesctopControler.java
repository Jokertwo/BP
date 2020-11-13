package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import my.bak.trafic.core.GuiceFXMLLoader;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.Event;
import my.bak.trafic.core.event.EventType;
import my.bak.trafic.core.event.types.AlertEvent;
import my.bak.trafic.core.event.types.ConnectionEvent;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.database.Database;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.ControlerContants;
import my.bak.trafic.view.CustomTab;
import my.bak.trafic.view.TabController;
import my.bak.trafic.view.util.ViewUtil;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Main controller of hole application, this class is launched at start
 *
 * @author Petr Lastovka
 */
public class DesctopControler implements Initializable {

    @InjectLogger
    private Logger logger;
    @FXML
    private TabPane tabPane;
    @FXML
    private MenuItem newTable;
    @FXML
    private MenuItem newImport;
    @FXML
    private MenuItem connect;
    @FXML
    private MenuItem disconnect;

    private Database database;
    private ThreadPool pool;

    private Scene scene;
    private Bus bus;
    private Preferences preferences;
    private ViewUtil util;


    @Inject
    public DesctopControler(Database database, ThreadPool pool, Bus bus, Preferences preferences, ViewUtil util) {
        super();
        this.database = database;
        this.pool = pool;
        this.bus = bus;
        this.preferences = preferences;
        this.util = util;
        bus.registerEventHandler(EventType.CONNECTION_STATUS, this::connectionEvent);
        bus.registerEventHandler(EventType.ALERT, this::allertEvent);
    }


    public void setScene(Scene scene) {
        this.scene = scene;
        inicializeKeyStroke();
    }


    /**
     * Handler for connection event. Based on value of connection event are disabled buttons in menu
     *
     * @param event {@link ConnectionEvent}
     */
    private void connectionEvent(Event event) {
        if (event instanceof ConnectionEvent) {
            pool.getFXExecutor().execute(() -> {
                boolean isConnected = ((ConnectionEvent) event).isConnected();
                connect.setDisable(isConnected);
                disconnect.setDisable(!isConnected);
                tabPane.getTabs().stream().map(item -> (CustomTab) item)
                        .forEach(value -> value.getController().setDisable(!isConnected));

            });
        }
    }


    /**
     * Handler for alert event. After receive alert event is shown like popup
     *
     * @param event {@link AlertEvent}
     */
    private void allertEvent(Event event) {
        if (event instanceof AlertEvent) {
            pool.getFXExecutor().execute(() -> {
                AlertEvent aEvent = (AlertEvent) event;
                openModalWindow(aEvent.getType(), aEvent.getTitleHeader(), aEvent.getContent(),
                        util.createStaceTrace(aEvent.getException()));
            });
        }
    }


    /**
     * Open preferences tab
     *
     * @param event <b>parameter is not used</b>
     */
    @FXML
    public void handlePreferences(ActionEvent event) {
        logger.info("Opening setting window");
        Optional<CustomTab> tab = tabPane.getTabs().stream()
                .filter(CustomTab.class::isInstance)
                .map(CustomTab.class::cast)
                .filter(item -> item.getController() instanceof SettingTabControler)
                .findFirst();
        if (tab.isPresent()) {
            tabPane.getSelectionModel().select(tab.get());
        } else {
            addNewTab(ControlerContants.SETTING_TAB_CONTENT);
        }
    }


    /**
     * Load tab from fxml and return his controller
     *
     * @param fxmlInputStream input stream to fxml file
     * @return controller {@link TabController}
     * @throws IOException IOException throws when file cannot be opened
     */
    private TabController getController(InputStream fxmlInputStream) throws IOException {
        GuiceFXMLLoader loader = GuiceFXMLLoader.getInstance();
        loader.load(fxmlInputStream);
        return loader.getController();
    }

    /**
     * One new tab with specific content
     *
     * @param path path to .fxml file
     */
    private void addNewTab(String path) {
        CustomTab tab = new CustomTab();
        try (InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream(path)) {
            TabController controller = getController(fxmlInputStream);
            tab.setClosable(true);
            tab.setContent(controller.getContent());
            tab.setText(controller.title());
            tab.setController(controller);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            controller.setDisable(!database.isConnected());
            pool.execute(() -> bus.publishEvent(new StatusChangeEvent("Open new " + controller.title(),
                    "Open new tab with " + controller.title() + " content")));
        } catch (IOException e) {
            logger.error("Unable to load content for table tab", e);
            bus.publishEvent(new StatusChangeEvent("Open tab error", "Unable to create because: " + e.getMessage()));
        }
    }


    /**
     * Open view tab
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleNewTable(ActionEvent event) {
        logger.info("Opening new view tab");
        addNewTab(ControlerContants.TABLE_TAB_CONTENT);
    }


    /**
     * Close all opened tab
     */
    public void closeAllTab() {
        while (!tabPane.getTabs().isEmpty()) {
            closeTabs(tabPane.getTabs().get(0));
        }
    }


    /**
     * Close selected tab
     */
    public void closeSelectedTab() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if (tab != null) {
            closeTabs(tab);
        }
    }


    /**
     * Close tab
     */
    public void closeTabs(Tab tab) {
        TabPaneBehavior behavior = ((TabPaneSkin) tabPane.getSkin()).getBehavior();
        if (behavior.canCloseTab(tab) && ((CustomTab) tab).getController().close()) {
            logger.info("Closing tab: {}", tab.getText());
            pool.execute(() -> bus.publishEvent(
                    new StatusChangeEvent("Closing tab " + tab.getText(), "Was closed tab with title: " + tab.getText())));
            behavior.closeTab(tab);
        }
    }


    /**
     * Open import tab
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleNewImport(ActionEvent event) {
        logger.info("Opening new import tab");
        addNewTab(ControlerContants.IMPORT_TAB_CONTENT);
    }


    /**
     * Open tab with manual insert
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleManualInsert(ActionEvent event) {
        logger.info("Openig new Manual insert tab");
        addNewTab(ControlerContants.MANUAL_INSERT_CONTENT);
    }


    /**
     * Create and register key strokes
     */
    private void inicializeKeyStroke() {
        logger.info("Creating content desktop");
        // new tab
        KeyCombination ctrlN = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination ctrlW = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination newImport = new KeyCodeCombination(KeyCode.I, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination closeAll = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.SHIFT_DOWN,
                KeyCodeCombination.CONTROL_DOWN);

        scene.setOnKeyPressed(event -> {
            if (ctrlN.match(event)) {
                handleNewTable(null);
            } else if (ctrlW.match(event)) {
                closeSelectedTab();
            } else if (newImport.match(event)) {
                handleNewImport(null);
            } else if (closeAll.match(event)) {
                closeAllTab();
            }
        });

        if (preferences.getStartConnected()) {
            logger.info("Starting app connected to db");
            handleMenuConnect(null);
        }
    }


    /**
     * Open tab with export content
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleExport(ActionEvent event) {
        logger.info("Openig new Manual insert tab");
        addNewTab(ControlerContants.EXPORT_TAB_CONTENT);
    }


    /**
     * Show alert window
     *
     * @param type    type of alert window {@link AlertType}
     * @param title   alert title
     * @param content alert message
     * @param detail  Optional<TitledPane> content with more information about error
     */
    private void openModalWindow(AlertType type, String title, String content, Optional<TitledPane> detail) {
        Alert warn = new Alert(type);
        warn.setTitle(type.name());
        warn.setHeaderText(title);
        VBox box = new VBox();
        box.setFillWidth(true);
        box.getChildren().add(new Label(content));
        warn.setResizable(true);
        detail.ifPresent(node -> box.getChildren().add(node));
        warn.getDialogPane().setContent(box);
        warn.showAndWait();
    }


    /**
     * Connect to db
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleMenuConnect(ActionEvent event) {
        connect.setDisable(true);
        pool.getCommonExecutor().execute(() -> {
            try {
                bus.publishEvent(new StatusChangeEvent("Connecting...", "Connecting to database"));
                database.connect();
            } catch (Exception e) {
                connect.setDisable(false);
                if (e instanceof SQLInvalidAuthorizationSpecException) {
                    bus.publishEvent(new StatusChangeEvent("Error", e.getMessage()));
                    openModalWindow(AlertType.WARNING, "Authorization error", e.getMessage(), util.createStaceTrace(e));
                    logger.error("Error during autorization", e);
                } else {
                    logger.fatal("Unknown error during connetion to database", e);
                    throw e;
                }
            }
        });
    }


    /**
     * Disconnect from db
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleMenuDisconect(ActionEvent event) {
        pool.getCommonExecutor().execute(() -> {
            bus.publishEvent(new StatusChangeEvent("Disconnecting...", "Disconnecting from database"));
            database.disconect();

        });
    }


    /**
     * Close application
     *
     * @param event <b>parameter is not used</b>
     */
    public void handleMenuExit(ActionEvent event) {
        Platform.exit();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // enable closing of tabs
        tabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

    }

}
