package my.bak.trafic.logger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public class Log4JTypeListener implements TypeListener {
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
            if (field.getType() == Logger.class && field.isAnnotationPresent(InjectLogger.class)) {
                if (Modifier.isStatic(field.getModifiers())) {
                    // use reflection
                    try {
                        field.setAccessible(true);
                        Logger logger = LogManager.getLogger(field.getDeclaringClass());
                        field.set(null, logger);
                    } catch (IllegalAccessException iae) {
                        iae.printStackTrace();
                    }
                } else {
                    // register a member injector
                    Log4JMembersInjector<T> memberInjector = new Log4JMembersInjector<>(field);
                    typeEncounter.register(memberInjector);
                }
            }
        }
    }
}
