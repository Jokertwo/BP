package my.bak.trafic.database;

import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.AlertEvent;
import my.bak.trafic.core.event.types.ConnectionEvent;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertEquals;


public class DatabaseProviderTest {

    private DatabaseProvider provider;
    private EntityManagerFactoryProvider myFactory;
    private Bus bus;
    private EntityManager manager;
    private EntityManagerFactory factory;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LoggerTestInjector.inject(DatabaseProvider.class);
    }


    @Before
    public void setUp() throws Exception {
        myFactory = EasyMock.createNiceMock(EntityManagerFactoryProvider.class);
        bus = EasyMock.createNiceMock(Bus.class);
        manager = EasyMock.createNiceMock(EntityManager.class);
        factory = EasyMock.createNiceMock(EntityManagerFactory.class);
        provider = new DatabaseProvider(myFactory, bus);
    }


    @Test
    public void testConnect() {
        prepareConnect();
        EasyMock.replay(manager, factory, myFactory, bus);
        provider.connect();
        EasyMock.verify(manager, factory, myFactory, bus);

    }


    private void prepareConnect() {
        EasyMock.expect(myFactory.getFatory()).andReturn(factory).once();
        EasyMock.expect(factory.createEntityManager()).andReturn(manager).once();

        bus.publishEvent(EasyMock.anyObject(ConnectionEvent.class));
        EasyMock.expectLastCall().once();

        bus.publishEvent(EasyMock.anyObject(StatusChangeEvent.class));
        EasyMock.expectLastCall().once();
    }


    @Test
    public void testDisconect() {
        prepareConnect();
        EasyMock.expect(manager.isOpen()).andReturn(true);
        manager.close();
        EasyMock.expectLastCall().once();

        bus.publishEvent(EasyMock.anyObject(ConnectionEvent.class));
        EasyMock.expectLastCall().once();

        bus.publishEvent(EasyMock.anyObject(StatusChangeEvent.class));
        EasyMock.expectLastCall().once();

        EasyMock.replay(manager, factory, myFactory, bus);
        provider.connect();
        provider.disconect();
        EasyMock.verify(manager, factory, myFactory, bus);
    }

    @Test
    public void testFailDisconnect() {
        prepareConnect();
        EasyMock.expect(manager.isOpen()).andReturn(false);
        EasyMock.replay(manager, factory, myFactory, bus);
        provider.connect();
        provider.disconect();
        EasyMock.verify(manager, factory, myFactory, bus);
    }


    @Test
    public void testFailConnect() {
        Exception e = new IllegalStateException("test");
        EasyMock.expect(myFactory.getFatory()).andThrow(e);
        bus.publishEvent(EasyMock.anyObject(StatusChangeEvent.class));
        EasyMock.expectLastCall().once();
        bus.publishEvent(EasyMock.anyObject(ConnectionEvent.class));
        EasyMock.expectLastCall().once();
        bus.publishEvent(EasyMock.anyObject(AlertEvent.class));

        EasyMock.replay(myFactory, bus);
        provider.connect();
        EasyMock.verify(myFactory, bus);
    }

    @Test
    public void testIsConnected() {
        prepareConnect();
        EasyMock.expect(manager.isOpen()).andReturn(true).once();
        EasyMock.replay(manager, factory, myFactory, bus);
        provider.connect();
        assertEquals(true, provider.isConnected());
        EasyMock.verify(manager, factory, myFactory, bus);
    }

    @Test
    public void testIsNotConnected() {
        prepareConnect();
        EasyMock.expect(manager.isOpen()).andReturn(false).once();
        EasyMock.replay(manager, factory, myFactory, bus);
        provider.connect();
        assertEquals(false, provider.isConnected());
        EasyMock.verify(manager, factory, myFactory, bus);
    }

    @Test
    public void testNullIsConnected() {
        assertEquals(false, provider.isConnected());
    }


}
