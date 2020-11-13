package my.bak.trafic.core.plugin.transport;

import com.google.inject.Inject;
import com.google.inject.Injector;
import my.bak.trafic.core.plugin.ImportPlugin;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.database.Database;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.RollbackException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class ImportDataImpl implements ImportData {

    private PluginProvider pluginProvider;
    private Database databaseProvider;
    private Injector injector;

    private boolean run = true;

    private List<ImportDataBuilder> cache;
    @InjectLogger
    private static Logger logger;
    private static Logger pluginLogger = LogManager.getLogger(ImportPlugin.class);


    @Inject
    public ImportDataImpl(PluginProvider pluginProvider,
                          Database databaseProvider,
                          Injector injector) {
        super();
        this.injector = injector;
        this.pluginProvider = pluginProvider;
        this.databaseProvider = databaseProvider;
        cache = new ArrayList<>();
    }


    public void importData(PluginInfo info,
                           String params,
                           Consumer<Boolean> consumer)
            throws WrongParametersException {

        Optional<ImportPlugin> pluginOp = pluginProvider.loadImportPlugin(info);
        if (pluginOp.isPresent()) {
            ImportPlugin plugin = pluginOp.get();
            run = true;

            // initialize plugins
            logger.info("Inicialize plugin: {}", plugin);
            plugin.init(pluginLogger);

            // inject dependencies
            logger.info("Setting injector to plugin");
            plugin.setUtility(injector);

            // set parameters
            logger.info("Setting parameters to plugin: {}", params);
            plugin.setParameters(params);

            // inform about start
            logger.info("Starting plugin");
            plugin.start();

            logger.info("Start importing data throw plugin");
            while (plugin.hasNext() && run && databaseProvider.isConnected()) {
                // import data via plugin
                save(plugin.importData());
            }
            if (run && databaseProvider.isConnected()) {
                // save rest
                save(cache);
            }

            // clear memory
            cache.clear();

            // inform about finish
            logger.info("finish work with plugin");
            plugin.finish();

            // release plugin's resources
            logger.info("dispose plugin");
            plugin.dispose();
            plugin = null;
            consumer.accept(true);
        } else {
            logger.error("Can not load plugin: {}, on path: {} ", info, info.getPluginPath());
            consumer.accept(false);
        }

    }


    private void save(ImportDataBuilder data) {
        cache.add(data);
        if (cache.size() % 1000 == 0) {
            save(cache);
            cache.clear();
        }
    }


    private void save(List<ImportDataBuilder> data) throws RollbackException {
        if (databaseProvider.isConnected()) {
            databaseProvider.save(data);
        }
    }


    @Override
    public void stopImport() {
        run = false;
    }
}
