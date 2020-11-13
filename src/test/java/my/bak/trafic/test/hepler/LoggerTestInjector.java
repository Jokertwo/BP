package my.bak.trafic.test.hepler;

import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;


public class LoggerTestInjector {
    static {
        System.setProperty("log4j.configurationFile",
                "log/logSetting.xml");
    }

    private static Logger logger;


    public static void inject(Object instance) {
        logger = LogManager.getLogger(instance.getClass());
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectLogger.class)) {
                field.setAccessible(true); // should work on private fields
                try {
                    field.set(instance, logger);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void inject(Class<?> clazz) {
        logger = LogManager.getLogger(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectLogger.class)) {
                field.setAccessible(true); // should work on private fields
                try {
                    field.set(clazz, logger);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
