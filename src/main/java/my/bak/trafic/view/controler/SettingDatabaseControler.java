package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.Setting;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class SettingDatabaseControler implements Initializable, Setting {
    @InjectLogger
    private static Logger logger;

    @FXML
    private VBox content;

    @FXML
    private TextField dbNameTF;

    @FXML
    private TextField userNameTF;

    @FXML
    private TextField pwdTF;

    @FXML
    private Button saveBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private CheckBox startWithCon;

    private Preferences pref;


    @Inject
    public SettingDatabaseControler(Preferences pref, ThreadPool pool, Bus bus) {
        super();
        this.pref = pref;
    }


    @FXML
    public void handleReset(ActionEvent event) {
        logger.info("Reset database credentials");
        loadValues();
    }


    @FXML
    public void handleSave(ActionEvent event) {
        save();
    }


    @Override
    public void save() {
        pref.setPassword(pwdTF.getText());
        pref.setDBAdress(dbNameTF.getText());
        pref.setUserName(userNameTF.getText());
        pref.setStartConnected(startWithCon.isSelected());
        pref.save();
    }


    @Override
    public Node getContent() {
        return content;
    }


    private void loadValues() {
        logger.info("Inicialize database credentials from properties");
        dbNameTF.setText(pref.getDBAddress());
        pwdTF.setText(pref.getPassword());
        userNameTF.setText(pref.getUserName());
        startWithCon.setSelected(pref.getStartConnected());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadValues();
    }


    @Override
    public String toString() {
        return "Database";
    }

}
