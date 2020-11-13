package my.bak.trafic.core.plugin.transport;

import my.bak.trafic.core.plugin.ImportPlugin;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.database.Database;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.apache.logging.log4j.core.Logger;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


public class ImportDataImplTest {

    private ImportData importData;
    private Database database;
    private PluginProvider pluginProvider;
    private ImportPlugin plugin;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LoggerTestInjector.inject(ImportDataImpl.class);
    }


    @Before
    public void setUp() throws Exception {
        pluginProvider = EasyMock.createNiceMock(PluginProvider.class);
        database = EasyMock.createNiceMock(Database.class);
        plugin = EasyMock.createNiceMock(ImportPlugin.class);

    }


    @Test(expected = WrongParametersException.class)
    public void testWrongParameter() throws WrongParametersException {
        EasyMock.expect(pluginProvider.loadImportPlugin(null)).andReturn(Optional.of(plugin));
        plugin.init(EasyMock.anyObject(Logger.class));
        EasyMock.expectLastCall();
        plugin.setUtility(null);
        EasyMock.expectLastCall();
        plugin.setParameters(null);
        EasyMock.expectLastCall().andThrow(new WrongParametersException(""));

        EasyMock.replay(pluginProvider, plugin);
        importData = new ImportDataImpl(pluginProvider, null, null);
        importData.importData(null, null, value -> {
        });
    }


    @Test
    public void testMissingPlugin() throws WrongParametersException {
        EasyMock.expect(pluginProvider.loadImportPlugin(EasyMock.anyObject(PluginInfo.class)))
                .andReturn(Optional.empty()).once();
        EasyMock.expect(plugin.getName()).andReturn("name").once();
        EasyMock.replay(pluginProvider, plugin);

        PluginInfo info = new PluginInfo(plugin, new File("path/to/nowhere"));

        importData = new ImportDataImpl(pluginProvider, null, null);

        importData.importData(info, null, value -> assertEquals(false, value));
        EasyMock.verify(pluginProvider, plugin);
    }


    @Test
    public void testSuccesImport() throws WrongParametersException {
        EasyMock.expect(pluginProvider.loadImportPlugin(null)).andReturn(Optional.of(plugin));
        plugin.init(EasyMock.anyObject(Logger.class));
        EasyMock.expectLastCall().once();
        plugin.setUtility(null);
        EasyMock.expectLastCall().once();
        plugin.setParameters(null);
        EasyMock.expectLastCall().once();
        plugin.start();
        EasyMock.expectLastCall().once();

        EasyMock.expect(plugin.hasNext()).andReturn(true).once().andReturn(false).once();
        EasyMock.expect(database.isConnected()).andReturn(true).times(3);
        List<ImportDataBuilder> list = new ArrayList<>();
        ImportDataBuilder builder = new ImportDataBuilder();
        list.add(builder);
        EasyMock.expect(plugin.importData()).andReturn(builder);
        database.save(list);
        EasyMock.expectLastCall().once();

        plugin.finish();
        EasyMock.expectLastCall().once();

        plugin.dispose();
        EasyMock.expectLastCall().once();
        EasyMock.replay(database, plugin, pluginProvider);
        importData = new ImportDataImpl(pluginProvider, database, null);
        importData.importData(null, null, value -> assertEquals(true, value));
        EasyMock.verify(database, plugin, pluginProvider);
    }

}
