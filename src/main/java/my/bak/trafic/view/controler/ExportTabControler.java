package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.AlertEvent;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.core.plugin.transport.ExportData;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.database.query.Query;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.clipboard.Clipboard;
import org.apache.logging.log4j.Logger;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ExportTabControler extends AbstractContentControler {

    @InjectLogger
    private static Logger logger;

    @FXML
    private TextArea pathTf;

    @FXML
    private Button fileChBtn;

    @FXML
    private ComboBox<PluginInfo> pluginsCb;

    @FXML
    private Label infoLabel;

    @FXML
    private ProgressIndicator progressIn;

    @FXML
    private Button stopBtn;

    private ThreadPool pool;
    private Query query;
    private Preferences preferences;
    private PluginProvider pluginProvider;
    private ExportData exportData;
    private Bus bus;


    @Inject
    public ExportTabControler(ThreadPool pool,
                              Query query,
                              Clipboard clipboard,
                              Preferences preferences,
                              PluginProvider pluginProvider,
                              ExportData exportData,
                              Bus bus) {
        super(logger, clipboard);
        this.pool = pool;
        this.query = query;
        this.preferences = preferences;
        this.pluginProvider = pluginProvider;
        this.exportData = exportData;
        this.bus = bus;
    }


    @Override
    public String title() {
        return "Export";
    }


    @Override
    public boolean close() {
        return true;
    }


    @Override
    public void handleRunBtn(ActionEvent event) {
        getRunBtn().setDisable(true);
        progressIn.setProgress(-1);
        progressIn.setVisible(true);
        stopBtn.setDisable(false);
        pool.execute(() -> {
            try {
                // create copy
                List<Column> copyColums = getQueryControler().getColumns().stream().collect(Collectors.toList());
                CriteriaQuery<Tuple> select = query.createSelectQuery(copyColums, getQueryControler().getRoot(),
                        getQueryControler().isDistinct());

                exportData.exportData(pluginsCb.getValue(), pathTf.getText().trim(), select, copyColums, handler -> {
                    if (handler) {
                        pool.getFXExecutor().execute(() -> {
                            getRunBtn().setDisable(false);
                            progressIn.setProgress(1);
                            stopBtn.setDisable(true);
                        });
                    }
                });

            } catch (WrongParametersException e) {
                logger.warn("Wrong plugin parameters", e);
                bus.publishEvent(
                        new AlertEvent(AlertType.WARNING, "Wrong plugin parameter", e.getMessage(), e));
                pool.getFXExecutor().execute(() -> {
                    progressIn.setProgress(1);
                    getRunBtn().setDisable(false);
                    stopBtn.setDisable(true);
                });
            } catch (Exception e) {

                bus.publishEvent(
                        new AlertEvent(AlertType.ERROR, "Fatal Error", e.getMessage(), e));
                pool.getFXExecutor().execute(() -> {
                    progressIn.setProgress(1);
                    getRunBtn().setDisable(false);
                    stopBtn.setDisable(true);
                });
            }
        });

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        getRunBtn().setText("Export");
        getRunBtn().setDisable(true);

        // fill combox with plugins
        pluginsCb.getItems().addAll(pluginProvider.getAvailableExportPlugins());

        stopBtn.setDisable(true);
    }


    @FXML
    void handleStopBtn(ActionEvent event) {
        logger.info("Stopping export");
        exportData.stop();
    }

}
