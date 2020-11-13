package my.bak.trafic.main;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.core.ThreadPoolImpl;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.EventBus;
import my.bak.trafic.core.plugin.loader.PluginLoaderFactory;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.core.plugin.transport.ExportData;
import my.bak.trafic.core.plugin.transport.ExportDataImpl;
import my.bak.trafic.core.plugin.transport.ImportData;
import my.bak.trafic.core.plugin.transport.ImportDataImpl;
import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.core.preferencies.PreferencesProp;
import my.bak.trafic.database.Database;
import my.bak.trafic.database.DatabaseProvider;
import my.bak.trafic.database.EntityManagerFactoryProvider;
import my.bak.trafic.database.MyEntityManagerFactory;
import my.bak.trafic.database.query.Query;
import my.bak.trafic.database.query.QueryBuilder;
import my.bak.trafic.logger.Log4JTypeListener;
import my.bak.trafic.view.clipboard.Clipboard;
import my.bak.trafic.view.clipboard.ClipboardImpl;
import my.bak.trafic.view.util.ViewUtil;
import my.bak.trafic.view.util.ViewUtilImpl;


public class ModuleManager extends AbstractModule {

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new Log4JTypeListener());
        bind(Database.class).to(DatabaseProvider.class).asEagerSingleton();
        bind(Bus.class).to(EventBus.class);
        bind(ThreadPool.class).to(ThreadPoolImpl.class);
        bind(Preferences.class).to(PreferencesProp.class);
        bind(PluginProvider.class).to(PluginLoaderFactory.class);
        bind(ImportData.class).to(ImportDataImpl.class);
        bind(ExportData.class).to(ExportDataImpl.class);
        bind(Query.class).to(QueryBuilder.class);
        bind(ViewUtil.class).to(ViewUtilImpl.class);
        bind(Clipboard.class).to(ClipboardImpl.class);
        bind(MyEntityManagerFactory.class).to(EntityManagerFactoryProvider.class);
    }
}
