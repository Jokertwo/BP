package my.bak.trafic.core;

import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;


public class GuiceFXMLLoader extends FXMLLoader {

    private static Injector injector;


    private GuiceFXMLLoader(Injector injector) {
        super();

        setLocation(getClass().getClassLoader().getResource("my/bak/trafic/view/controler/"));
        setControllerFactory(injector::getInstance);
    }


    public static void create(final Injector injector) {
        GuiceFXMLLoader.injector = injector;
    }


    public static GuiceFXMLLoader getInstance() {
        if (injector == null) {
            throw new IllegalStateException("Loader was not created");
        } else {
            return new GuiceFXMLLoader(injector);
        }
    }

}
