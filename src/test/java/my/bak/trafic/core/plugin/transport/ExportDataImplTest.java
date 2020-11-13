package my.bak.trafic.core.plugin.transport;

import my.bak.trafic.core.plugin.ExportPlugin;
import my.bak.trafic.core.plugin.exception.WrongParametersException;
import my.bak.trafic.core.plugin.loader.PluginProvider;
import my.bak.trafic.database.Database;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Optional;


public class ExportDataImplTest {

    private ExportData exportData;

    private PluginProvider pluginProviderMock;
    private Database databaseMock;
    private ExportPlugin pluginMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @BeforeClass
    public static void tearUp() {
        LoggerTestInjector.inject(ExportDataImpl.class);
    }


    @Before
    public void setUp() throws Exception {
        pluginProviderMock = EasyMock.createNiceMock(PluginProvider.class);
        databaseMock = EasyMock.createNiceMock(Database.class);
        pluginMock = EasyMock.createNiceMock(ExportPlugin.class);
    }


    @Test
    public void testCannotLoadPlugin() throws Exception {
        EasyMock.expect(pluginProviderMock.loadExportPlugin(null)).andReturn(Optional.empty()).once();
        EasyMock.replay(pluginProviderMock);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Plugin cannot be loaded");
        exportData = new ExportDataImpl(pluginProviderMock, null, databaseMock);
        exportData.exportData(null, null, null, null, empty -> {
        });

        EasyMock.verify(pluginProviderMock);
    }


    @Test
    public void testWrongPluginParameters() throws Exception {
        EasyMock.expect(pluginProviderMock.loadExportPlugin(null)).andReturn(Optional.of(pluginMock));

        pluginMock.init(null);
        EasyMock.expectLastCall().once();

        pluginMock.setUtility(null);
        EasyMock.expectLastCall().once();

        pluginMock.setParameters(null);
        EasyMock.expectLastCall().andThrow(new WrongParametersException("message"));

        EasyMock.replay(pluginProviderMock, pluginMock);

        thrown.expect(WrongParametersException.class);
        thrown.expectMessage("message");
        exportData = new ExportDataImpl(pluginProviderMock, null, databaseMock);
        exportData.exportData(null, null, null, null, empty -> {
        });
    }


    @Test
    public void testExportData() throws Exception {
        EasyMock.expect(pluginProviderMock.loadExportPlugin(null)).andReturn(Optional.of(pluginMock));

        pluginMock.init(EasyMock.anyObject(Logger.class));
        EasyMock.expectLastCall().once();

        pluginMock.setUtility(null);
        EasyMock.expectLastCall().once();

        pluginMock.setParameters(null);
        EasyMock.expectLastCall().once();

        // simulation database connection
        EntityManager manager = EasyMock.createNiceMock(EntityManager.class);
        Session session = EasyMock.createNiceMock(Session.class);
        Query<Tuple> query = EasyMock.createNiceMock(Query.class);
        ScrollableResults resolutScroll = EasyMock.createNiceMock(ScrollableResults.class);
        Tuple tuple = EasyMock.createNiceMock(Tuple.class);
        CriteriaQuery<Tuple> criteriaQuery = null;

        EasyMock.expect(databaseMock.getEntityManager()).andReturn(manager).once();

        EasyMock.expect(manager.unwrap(Session.class)).andReturn(session).once();

        EasyMock.expect(session.createQuery(criteriaQuery)).andReturn(query).once();

        EasyMock.expect(query.scroll(ScrollMode.FORWARD_ONLY)).andReturn(resolutScroll).once();
        // first time return true for of loop, second time to break loop
        EasyMock.expect(resolutScroll.next()).andReturn(true).once().andReturn(false).once();

        EasyMock.expect(resolutScroll.get()).andReturn(new Object[]{tuple});
        // end of database simulation

        pluginMock.exportData(EasyMock.anyObject(ExportDataBuilder.class));
        EasyMock.expectLastCall().once();

        resolutScroll.close();
        EasyMock.expectLastCall().once();

        pluginMock.finish();
        EasyMock.expectLastCall().once();

        pluginMock.dispose();
        EasyMock.expectLastCall().once();

        EasyMock.replay(pluginProviderMock,
                databaseMock,
                pluginMock,
                manager,
                session,
                query,
                resolutScroll,
                tuple);
        exportData = new ExportDataImpl(pluginProviderMock, null, databaseMock);
        exportData.exportData(null, null, null, new ArrayList<>(), empty -> {
        });

        EasyMock.verify(pluginProviderMock,
                databaseMock,
                pluginMock,
                manager,
                session,
                query,
                resolutScroll,
                tuple);
    }

}
