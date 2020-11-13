package my.bak.trafic.core.plugin;

import com.google.inject.Injector;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import org.apache.logging.log4j.Logger;


public interface Plugin {

    /**
     * Return plugin name
     *
     * @return plugin name
     */
    String getName();


    /**
     * Return plugin description
     *
     * @return String plugin description
     */
    String getDescription();


    /**
     * Set injector. Throw injector it is possible to get each available instance.
     *
     * @param injector google guice injector
     */
    void setUtility(Injector injector);


    /**
     * Initialize plugin resources and prepare
     */
    void init(Logger logger);


    /**
     * Set parameters to plugin
     *
     * @param parameters plugin parameter
     */
    void setParameters(String parameters) throws WrongParametersException;


    /**
     * Start using of plugin
     */
    void start();


    /**
     * End of plugin using
     */
    void finish();


    /**
     * Deiniticialize plugin after export/import
     */
    void dispose();


    /**
     * Return type of plugin
     *
     * @return
     */
    PluginFlag getPluginFlag();
}
