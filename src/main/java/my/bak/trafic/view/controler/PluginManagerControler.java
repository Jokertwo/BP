package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.AlertEvent;
import my.bak.trafic.core.plugin.exception.NotSupportedPlugin;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.PluginCellFactory;
import my.bak.trafic.view.Setting;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Optional;
import java.util.ResourceBundle;


public class PluginManagerControler implements Initializable, Setting {

    @InjectLogger
    private static Logger logger;

    @FXML
    private VBox content;

    @FXML
    private ListView<PluginInfo> pluginListView;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label infoLabel;

    @FXML
    private Label staticInfo;

    private PluginProvider pluginProvider;
    private ThreadPool pool;
    private Preferences preferences;
    private Bus bus;


    @Inject
    public PluginManagerControler(PluginProvider pluginProvider, ThreadPool pool, Preferences preferences, Bus bus) {
        super();
        this.pluginProvider = pluginProvider;
        this.pool = pool;
        this.preferences = preferences;
        this.bus = bus;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPluginList();
        pluginListView.getSelectionModel().selectedItemProperty().addListener((prop, oldValue, newValue) -> {
            if (newValue != null) {
                logger.debug("Selected value in plugin list: {}", newValue);
                infoLabel.setText(newValue.getDescription());
                staticInfo.setVisible(true);
            }
        });
        pluginListView.setCellFactory(pluginWrapper -> new PluginCellFactory());
        infoLabel.setWrapText(true);
    }


    private void initPluginList() {
        pluginListView.getSelectionModel().clearSelection();
        pluginListView.setItems(createPluginList());
    }


    /**
     * Action for add new plugin
     *
     * @param event
     */
    public void handleAddButton(ActionEvent event) {
        logger.debug("Click on {} button", addButton.getText());
        FileChooser fileChooser = new FileChooser();
        String path = preferences.getLastOpenedFile();
        if (path != null && !path.trim().isEmpty()) {
            fileChooser.setInitialDirectory(new File(path));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Jar Files", "*.jar"));
        fileChooser.setTitle("Select plugin");
        File file = fileChooser.showOpenDialog(pluginListView.getScene().getWindow());
        if (file != null) {
            preferences.setLastOpenedFile(file.getParentFile().getAbsolutePath());
            pool.getCommonExecutor().execute(() -> {
                try {
                    pluginProvider.savePlugin(file);
                    pool.getFXExecutor().execute(this::initPluginList);

                } catch (FileAlreadyExistsException e) {
                    logger.error("Can not save plugin because file already exist");
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot add plugin because file on path:")
                            .append("\n")
                            .append(e.getMessage())
                            .append("\n")
                            .append("already exist");
                    bus.publishEvent(new AlertEvent(AlertType.WARNING, "Plugin already exist", sb.toString()));
                } catch (NotSupportedPlugin e) {
                    logger.error("File on path: {}, is not supported plugin", file.getAbsolutePath());
                    bus.publishEvent(new AlertEvent(AlertType.WARNING, "Not supported plugin",
                            "File on path: '" + file.getAbsolutePath() + "' is not supported plugin", e));
                } catch (IOException e) {
                    logger.error("Error during loading new plugin", e);
                }
            });

        }
    }


    public void handleDeleteButton(ActionEvent event) {
        logger.debug("Click on {} button", deleteButton.getText());
        PluginInfo wrapper = pluginListView.getSelectionModel().getSelectedItem();
        if (wrapper != null) {
            Optional<ButtonType> result = confirmDelete(wrapper);
            // handle repose from user
            if (result.get() == ButtonType.OK) {
                pool.execute(() -> {
                    try {
                        // remove plugin
                        pluginProvider.deletePlugin(wrapper);
                        // update plugin list
                        pool.getFXExecutor().execute(this::initPluginList);
                    } catch (IOException e) {
                        logger.error("Error during delete plugin: {}, on path: {}", wrapper.getName(),
                                wrapper.getPluginPath(), e);
                        bus.publishEvent(
                                new AlertEvent(AlertType.WARNING, "Delete plugin", "Plugin cannot be deleted", e));
                    }
                });
            }
        }
    }


    /**
     * Show confirm dialog for removing plugin
     *
     * @param wrapper
     * @return
     */
    private Optional<ButtonType> confirmDelete(PluginInfo wrapper) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete plugin");
        alert.setHeaderText("Delete plugin '" + wrapper.getName() + "'");
        alert.setContentText("Are you sure that you want to delete this plugin?");
        return alert.showAndWait();
    }


    /**
     * Create observable list from plugin names
     *
     * @return
     */
    private ObservableList<PluginInfo> createPluginList() {
        ObservableList<PluginInfo> items = FXCollections.observableArrayList();
        pluginProvider.getAvailableImportPlugins().forEach(items::add);
        pluginProvider.getAvailableExportPlugins().forEach(items::add);
        return items;
    }


    public void handleSave(ActionEvent event) {
        save();
    }


    @Override
    public void save() {
        // TODO Auto-generated method stub

    }


    @Override
    public Node getContent() {
        return content;
    }


    @Override
    public String toString() {
        return "Plugins";
    }

}
