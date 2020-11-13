package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.core.plugin.transport.ImportDataBuilder;
import my.bak.trafic.database.Database;
import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.TabController;
import org.apache.logging.log4j.Logger;
import tornadofx.control.DateTimePicker;

import java.net.URL;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


public class ManualViewTabControler implements TabController, Initializable {

    @InjectLogger
    private static Logger logger;

    @FXML
    private BorderPane content;

    @FXML
    private GridPane gridPane;

    @FXML
    private ComboBox<String> placeCB;

    @FXML
    private ComboBox<String> directionCB;

    @FXML
    private ComboBox<String> typeCB;

    @FXML
    private TextField valueTF;

    @FXML
    private Button insertBTN;

    @FXML
    private Button clearBTN;

    @FXML
    private Button refreshBTN;

    private DateTimePicker beginDP;
    private DateTimePicker endDP;

    private ThreadPool pool;
    private Database database;
    private Bus bus;


    @Inject
    public ManualViewTabControler(ThreadPool pool, Database database, Bus bus) {
        this.pool = pool;
        this.database = database;
        this.bus = bus;
        beginDP = new DateTimePicker();
        beginDP.setMaxWidth(Integer.MAX_VALUE);
        endDP = new DateTimePicker();
        endDP.setMaxWidth(Integer.MAX_VALUE);
    }


    @FXML
    void handleClearBtn(ActionEvent event) {
        logger.info("Clear all fields");
        beginDP.setValue(null);
        endDP.setValue(null);
        typeCB.setValue(null);
        placeCB.setValue(null);
        directionCB.setValue(null);
        valueTF.setText(null);
    }


    @FXML
    void handleRefreshBtn(ActionEvent event) {
        refreshCB();
    }


    @FXML
    void handleInsertBtn(ActionEvent event) {
        Map<String, String> value = new HashMap<>();
        value.put(typeCB.getValue(), valueTF.getText());

        pool.execute(() -> {
            bus.publishEvent(new StatusChangeEvent("Inserting...", "Inserting value to database"));
            logger.info("Inserting to db: begin: {}, end: {}, place: {}, direction: {},type/value: {}  ",
                    Date.from(beginDP.getDateTimeValue().atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(endDP.getDateTimeValue().atZone(ZoneId.systemDefault()).toInstant()),
                    directionCB.getValue(),
                    placeCB.getValue(),
                    value);
            if (database.isConnected()) {
                database.save(Arrays.asList(new ImportDataBuilder()
                        .setBeginDate(Date.from(beginDP.getDateTimeValue().atZone(ZoneId.systemDefault()).toInstant()))
                        .setEndDate(Date.from(endDP.getDateTimeValue().atZone(ZoneId.systemDefault()).toInstant()))
                        .setDirection(directionCB.getValue())
                        .setPlace(placeCB.getValue())
                        .setData(value)));
                bus.publishEvent(new StatusChangeEvent("Insert done", ""));
                refreshCB();
            }
        });
    }


    @Override
    public String title() {
        return "Manual insert";
    }


    @Override
    public boolean close() {
        return true;
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
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.add(beginDP, 1, 0);
        gridPane.add(endDP, 1, 1);
        refreshCB();

        insertBTN.disableProperty()
                .bind(beginDP.dateTimeValueProperty().isNull()
                        .or(endDP.dateTimeValueProperty().isNull())
                        .or(placeCB.valueProperty().isNull()).or(directionCB.valueProperty().isNull())
                        .or(placeCB.valueProperty().isNull())
                        .or(Bindings.createBooleanBinding(() -> valueTF.getText().trim().isEmpty(), valueTF.textProperty())));
    }


    private void refreshCB() {
        pool.execute(() -> {
            if (database.isConnected()) {
                logger.info("Updating content of all comboboxis");
                List<Location> locations = database.getAllLocation();
                List<String> places = locations.stream().map(Location::getPlace).collect(Collectors.toList());
                List<String> directions = locations.stream().map(Location::getDirection)
                        .collect(Collectors.toList());
                List<String> types = database.getAllTypeColumns().stream().map(Type::getType)
                        .collect(Collectors.toList());
                pool.getFXExecutor().execute(() -> {
                    updateCB(types, typeCB);
                    updateCB(directions, directionCB);
                    updateCB(places, placeCB);
                });
            }
        });
    }


    private void updateCB(List<String> newValues, ComboBox<String> comboBox) {
        String userValue = comboBox.getValue();
        comboBox.getItems().clear();
        comboBox.getItems().addAll(newValues);
        comboBox.setValue(userValue);
    }

}
