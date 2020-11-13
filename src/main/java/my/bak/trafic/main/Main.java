package my.bak.trafic.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import my.bak.trafic.core.GuiceFXMLLoader;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.view.ControlerContants;
import my.bak.trafic.view.controler.DesctopControler;
import my.bak.trafic.view.util.ViewUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Locale;


public class Main extends Application {

    static {
        System.setProperty("log4j.configurationFile",
                "log/logSetting.xml");
    }

    private static Logger logger = LogManager.getLogger();

    private static String PID;
    private static Injector injector;


    public static void main(String[] args) {
        PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        logger.info("Starting App\n\t time: {}\n\t pid: {}", LocalDateTime.now(), PID);
        injector = Guice.createInjector(new ModuleManager());
        Locale.setDefault(Locale.UK);
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        // last chance
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        // set injector to guice fxml loader
        GuiceFXMLLoader.create(injector);

        // load show main desktop view
        try (InputStream fxmlInputStream = Main.class.getClassLoader().getResourceAsStream(ControlerContants.DESKTOP_VIEW)) {
            GuiceFXMLLoader loader = GuiceFXMLLoader.getInstance();

            Parent parent = loader.load(fxmlInputStream);
            DesctopControler ctrl = loader.getController();

            Scene scene = new Scene(parent);
            ctrl.setScene(scene);
            primaryStage.setScene(scene);
            setMainIcon(primaryStage);
            primaryStage.setTitle("Trafic data manager");
            primaryStage.show();
        } catch (IOException ex) {
            logger.fatal("", ex);
            System.exit(1);
        }

    }


    /**
     * Set app icon
     *
     * @param primaryStage
     */
    private void setMainIcon(Stage primaryStage) {
        Image image = new Image("icons/Traffic man_0.svg.png");
        if (image != null) {
            primaryStage.getIcons().add(image);
        }
    }


    /**
     * exit application
     */
    @Override
    public void stop() throws Exception {
        injector.getInstance(Preferences.class).save();
        injector.getInstance(ThreadPool.class).shutDown();
        logger.info("Clossing App\n\t time: {}\n\t pid: {}", LocalDateTime.now(), PID);
        super.stop();
        System.exit(0);
    }


    private void uncaughtException(Thread t, Throwable e) {

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Fatal error");
            alert.setHeaderText("Fatal error during execution, You may need to restart the application");
            alert.setResizable(true);
            injector.getInstance(ViewUtil.class).createStaceTrace(e)
                    .ifPresent(node -> alert.getDialogPane().setContent(node));
            alert.showAndWait();
        });
        logger.fatal("Fatal error", e);
    }

}
