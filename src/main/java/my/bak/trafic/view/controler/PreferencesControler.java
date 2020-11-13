package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class PreferencesControler implements Initializable {

    @FXML
    private TextField dbAdress;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Button apply;

    @FXML
    private Button cancel;

    @InjectLogger
    private static Logger logger;

    private Preferences preferences;
    private ThreadPool pool;


    @Inject
    public PreferencesControler(Preferences preferences, ThreadPool pool) {
        super();
        this.preferences = preferences;
        this.pool = pool;
    }


    public void handleApplyButton(ActionEvent action) {
        pool.getCommonExecutor().execute(() -> {
            logger.info("Setting new preferences dbName: {},user: {},password: {}", dbAdress.getText().trim(),
                    userName.getText().trim(), password.getText().trim());
            preferences.setDBAdress(dbAdress.getText().trim());
            preferences.setUserName(userName.getText().trim());
            preferences.setPassword(password.getText().trim());
            preferences.save();
        });
        ((Stage) apply.getScene().getWindow()).hide();
    }


    public void handleResetButton(ActionEvent action) {
        dbAdress.setText(preferences.getDBAddress());
        userName.setText(preferences.getUserName());
        password.setText(preferences.getPassword());
    }


    public void handleCancleButton(ActionEvent action) {
        ((Stage) cancel.getScene().getWindow()).hide();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        password.setText(preferences.getPassword());
        userName.setText(preferences.getUserName());
        dbAdress.setText(preferences.getDBAddress());

    }

}
