package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import my.bak.trafic.core.GuiceFXMLLoader;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.ControlerContants;
import my.bak.trafic.view.Setting;
import my.bak.trafic.view.TabController;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class SettingTabControler implements Initializable, TabController {
    @InjectLogger
    private static Logger logger;

    @FXML
    private BorderPane content;

    @FXML
    private TreeView<Setting> settingTree;

    @FXML
    private BorderPane settingContent;


    @Inject
    public SettingTabControler() {
        super();
    }


    /**
     * Fill setting tree
     */
    private void fillSettingTree() {
        logger.info("Fill setting tree with values");

        logger.debug("Create root of setting tree");
        TreeItem<Setting> setting = new TreeItem<>();

        setting.setExpanded(true);

        // create database setting
        createNode(ControlerContants.SETTING_DATABASE_CONTENT)
                .ifPresent(node -> setting.getChildren().add(new TreeItem<>(node)));

        // create plugin setting
        createNode(ControlerContants.SETTING_PLUGIN_CONTENT)
                .ifPresent(node -> setting.getChildren().add(new TreeItem<>(node)));

        settingTree.setRoot(setting);
        settingTree.setShowRoot(false);

    }


    /**
     * Create node with setting
     *
     * @return Content with setting
     */
    private Optional<Setting> createNode(String path) {
        logger.debug("Creating setting node from : {}", path);
        Setting setting = null;
        try (InputStream fxmlInputStream = ClassLoader
                .getSystemResourceAsStream(path)) {
            GuiceFXMLLoader loader = GuiceFXMLLoader.getInstance();
            loader.load(fxmlInputStream);
            setting = loader.getController();
        } catch (Exception e) {
            logger.error("Can not create setting node: {}", path, e);
        }
        return Optional.ofNullable(setting);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillSettingTree();

        settingTree.getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> settingContent.setCenter(newValue.getValue().getContent()));
        settingTree.getSelectionModel().selectFirst();
    }


    @Override
    public String title() {
        return "Setting";
    }


    @Override
    public boolean close() {
        logger.info("Closing setting tab");
        return true;
    }


    @Override
    public Node getContent() {
        return content;
    }


    @Override
    public void setDisable(boolean disable) {
        logger.info("Setting tab can not be disabled");
    }

}
