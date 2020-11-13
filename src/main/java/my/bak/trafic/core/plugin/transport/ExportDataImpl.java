package my.bak.trafic.core.plugin.transport;

import com.google.inject.Inject;
import com.google.inject.Injector;
import my.bak.trafic.core.plugin.ExportPlugin;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.database.Database;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.database.query.visitor.impl.DataBuilderVisitor;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class ExportDataImpl implements ExportData {

    @InjectLogger
    private static Logger logger;
    private PluginProvider pluginProvider;
    private Injector injector;
    private ExportPlugin plugin;
    private Database database;
    private boolean stop = false;
    private static Logger pluginLogger = LogManager.getLogger(ExportPlugin.class);


    @Inject
    public ExportDataImpl(PluginProvider pluginProvider,
                          Injector injector,
                          Database database) {
        super();
        this.pluginProvider = pluginProvider;
        this.database = database;
        this.injector = injector;
        logger.info("Create plugin export proxy");
    }


    private void startExport(PluginInfo info) {
        Optional<ExportPlugin> pluginOp = pluginProvider.loadExportPlugin(info);
        if (pluginOp.isPresent()) {
            plugin = pluginOp.get();

            // initialize plugins
            logger.info("Inicialize plugin: {}", plugin);
            plugin.init(pluginLogger);

            // inject dependencies
            logger.debug("Setting injector to plugin");
            plugin.setUtility(injector);
        } else {
            logger.fatal("Plugin: {}, can not be loaded", info);
            throw new IllegalStateException("Plugin cannot be loaded");
        }

    }


    private void setParamsExport(String params) throws WrongParametersException {
        logger.info("Setting parameters to plugin: {}", params);
        plugin.setParameters(params);

        logger.debug("Starting plugin");
        plugin.start();
    }


    @Override
    public void exportData(PluginInfo info,
                           String params,
                           CriteriaQuery<Tuple> select,
                           List<Column> columns,
                           Consumer<Boolean> handler)
            throws Exception {

        stop = false;
        try {
            startExport(info);
            setParamsExport(params);
            Session session = database.getEntityManager().unwrap(Session.class);

            Query<Tuple> query = session.createQuery(select);
            ScrollableResults resultScroll = query.scroll(ScrollMode.FORWARD_ONLY);
            Visitor<ExportDataBuilder> visitor;
            while (resultScroll.next() && !stop) {
                Object row = resultScroll.get()[0];
                if (row instanceof Tuple) {
                    ExportDataBuilder data = new ExportDataBuilder();
                    visitor = new DataBuilderVisitor((Tuple) row, data);
                    for (Column column : columns) {
                        data = column.visit(visitor);
                    }
                    plugin.exportData(data);
                }
            }
            resultScroll.close();
            stopExport();
            handler.accept(true);
        } catch (WrongParametersException e) {
            // release plugin's resources
            logger.info("dispose plugin");
            plugin.dispose();
            // rethrow exception
            throw e;
        } catch (Exception e) {
            logger.fatal("Fatal error during export via plugin");
            // if plugin have some resources
            if (plugin != null) {
                plugin.dispose();
            }
            throw e;
        }
    }


    private void stopExport() {
        // inform about finish
        logger.info("Finish work with plugin");
        plugin.finish();

        // release plugin's resources
        logger.info("dispose plugin");
        plugin.dispose();

    }


    @Override
    public void stop() {
        stop = true;

    }

}
