package my.bak.trafic.core.preferencies;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.Properties;


@Singleton
public class PreferencesProp implements Preferences {

    @InjectLogger
    private static Logger logger;
    private static final String PASSWORD = "password";
    private static final String USERNAME = "userName";
    private static final String DB_ADRESS = "dbAdress";
    private static final String CONNECTED = "connected";
    private static final String LAST_FILE = "lastFile";
    private Properties prop;
    private Bus bus;


    @Inject
    public PreferencesProp(Bus bus) {
        this.bus = bus;
        createDefault();
        prop = new Properties();
        try (InputStream stream = new FileInputStream(PROP_FILE)) {
            prop.load(stream);
        } catch (IOException e) {
            logger.warn("Can not load properties", e);
        }
    }


    private void createDefault() {
        File file = new File(PROP_FILE);
        if (!file.exists()) {
            logger.info("No property file found, creating default property");
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("Can not create default property");
            }
        }
    }


    @Override
    public String getPassword() {
        return prop.getProperty(PASSWORD, "");
    }


    @Override
    public String getDBAddress() {
        return prop.getProperty(DB_ADRESS, "");
    }


    @Override
    public String getUserName() {
        return prop.getProperty(USERNAME, "");
    }


    @Override
    public void setPassword(String password) {
        prop.setProperty(PASSWORD, password);
    }


    @Override
    public void setUserName(String userName) {
        prop.setProperty(USERNAME, userName);
    }


    @Override
    public void setDBAdress(String address) {
        prop.setProperty(DB_ADRESS, address);
    }


    @Override
    public void save() {
        String saveComment = "Last save: " + new Date().toString();
        String status = "";
        try (OutputStream stream = new FileOutputStream(PROP_FILE)) {
            prop.store(stream, saveComment);
            status = "Preferences saved";
        } catch (IOException e) {
            logger.error("File for properties not exist and properties can not be saved.", e);
            status = "Preferences saved error";
        }
        bus.publishEvent(new StatusChangeEvent(status, ""));

    }


    @Override
    public boolean getStartConnected() {
        String connected = prop.getProperty(CONNECTED);
        return connected != null ? Boolean.parseBoolean(connected) : false;
    }


    @Override
    public void setStartConnected(boolean connected) {
        prop.setProperty(CONNECTED, String.valueOf(connected));
    }


    @Override
    public String getLastOpenedFile() {
        return prop.getProperty(LAST_FILE);
    }


    @Override
    public void setLastOpenedFile(String path) {
        prop.setProperty(LAST_FILE, path);

    }

}
