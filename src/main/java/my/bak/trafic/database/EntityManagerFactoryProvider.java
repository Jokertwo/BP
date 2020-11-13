package my.bak.trafic.database;

import com.google.inject.Inject;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;


public class EntityManagerFactoryProvider implements MyEntityManagerFactory {

    @InjectLogger
    private static Logger logger;
    private Preferences preferencies;


    @Inject
    public EntityManagerFactoryProvider(Preferences preferencies) {
        super();

        this.preferencies = preferencies;
    }


    @Override
    public EntityManagerFactory getFatory() {
        Map<String, String> params = new HashMap<>();
        params.put("javax.persistence.jdbc.url", "jdbc:hsqldb:file:" + preferencies.getDBAddress());
        params.put("javax.persistence.jdbc.user", preferencies.getUserName());
        params.put("javax.persistence.jdbc.password", preferencies.getPassword());

        logger.info(
                "Create new entity manager factory with user setting: database adress:{}, user name: {}, password: {}",
                preferencies.getDBAddress(), preferencies.getUserName(), preferencies.getPassword());
        return Persistence.createEntityManagerFactory("hibernate", params);
    }

}
