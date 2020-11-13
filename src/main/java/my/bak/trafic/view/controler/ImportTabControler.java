package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.AlertEvent;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.core.plugin.transport.ImportData;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.TabController;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;


public class ImportTabControler implements Initializable, TabController {

    @InjectLogger
    private static Logger logger;

    private Bus bus;
    private ThreadPool pool;
    private PluginProvider pluginProvider;
    private ImportData importData;

    @FXML
    private ProgressIndicator progressIn;

    @FXML
    private ComboBox<PluginInfo> pluginsCb;

    @FXML
    private TextArea pathTf;

    @FXML
    private Button fileChBtn;

    @FXML
    private Button importBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Label infoLabel;

    @FXML
    private BorderPane content;

    private AtomicBoolean inProgress;
    private Preferences preferences;


    @Inject
    public ImportTabControler(Bus bus,
                              ThreadPool pool,
                              PluginProvider pluginProvider,
                              ImportData importData,
                              Preferences preferences) {
        super();
        this.bus = bus;
        this.pool = pool;
        this.pluginProvider = pluginProvider;
        this.importData = importData;
        this.preferences = preferences;
        inProgress = new AtomicBoolean(false);
    }


    public void handleFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        String path = preferences.getLastOpenedFile();
        if (path != null && !path.trim().isEmpty()) {
            fileChooser.setInitialDirectory(new File(path));
        }
        File file = fileChooser.showOpenDialog(fileChBtn.getScene().getWindow());
        if (file != null) {
            preferences.setLastOpenedFile(file.getParentFile().getAbsolutePath());
            String text = pathTf.getText();
            text = text.trim().isEmpty() ? "" : text + "\n";
            text += file.getAbsolutePath();
            pathTf.setText(text);
            logger.info("Choosing file: {}", pathTf.getText());
        }
    }


    public void handleImport(ActionEvent event) {

        // disable import btn
        importBtn.setDisable(true);

        // show progress
        progressIn.setProgress(-1);
        progressIn.setVisible(true);
        infoLabel.setText("Importing...");
        infoLabel.setVisible(true);
        infoLabel.setWrapText(true);
        pool.getPluginExecutor().execute(() -> {
            try {
                importData.importData(pluginsCb.getValue(), pathTf.getText().trim(), (value -> {
                    if (value) {
                        logger.info("Succesfull import");
                        pool.getFXExecutor().execute(this::succesImport);
                        bus.publishEvent(new StatusChangeEvent("Import done...", "Importing via plugin was done"));
                    } else {
                        // something wrong during eecution
                        pool.getFXExecutor().execute(this::errorImport);
                        bus.publishEvent(
                                new StatusChangeEvent("Import error...", "During importing via plugin raised error"));
                        logger.error("Error during importing data");
                    }
                }));
            } catch (WrongParametersException e) {
                logger.error("Wrong plugin parameters", e);
                pool.getFXExecutor().execute(this::errorImport);
                bus.publishEvent(
                        new AlertEvent(AlertType.WARNING, "Wrong plugin parameter", e.getMessage(),
                                e));
            } catch (Exception e) {
                logger.fatal("Fatal unchecked error during import plugin execution", e);
                pool.getFXExecutor().execute(this::errorImport);
                bus.publishEvent(
                        new AlertEvent(AlertType.ERROR, "Plugin", "Fatal error during import data", e));

            }
        });
    }


    private void succesImport() {
        // enable new import
        importBtn.setDisable(false);
        // inform user that import was done
        infoLabel.setText("Import done");
        progressIn.setProgress(1);
    }


    private void errorImport() {
        // enable new import
        importBtn.setDisable(false);
        // information for user
        infoLabel.setText("Import error");
        progressIn.setProgress(1);
        progressIn.setVisible(false);
    }


    public void handleStop(ActionEvent event) {
        logger.debug("Stop inserting to db");
        importData.stopImport();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // fill combox with plugins
        pluginsCb.getItems().addAll(pluginProvider.getAvailableImportPlugins());

        // disable file chooser before u choose plugin
        fileChBtn.disableProperty().bind(pluginsCb.getSelectionModel().selectedItemProperty().isNull());

        // disable import btn before u choose plugin
        importBtn.setDisable(true);
        pluginsCb.getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> importBtn.setDisable(newValue == null));

        stopBtn.disableProperty()
                .bind(importBtn.disabledProperty().not().or(pluginsCb.getSelectionModel().selectedItemProperty().isNull()));

    }


    @Override
    public boolean close() {
        return !inProgress.get();
    }


    @Override
    public Node getContent() {
        return content;
    }


    @Override
    public void setDisable(boolean disable) {
        content.setDisable(disable);
    }


    @Override
    public String title() {
        return "Import";
    }

}
