package my.bak.trafic.database;

import my.bak.trafic.core.preferencies.Preferences;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.easymock.EasyMock;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class EntityManagerFactoryProviderTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LoggerTestInjector.inject(EntityManagerFactoryProvider.class);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MyEntityManagerFactory factory;
    private Preferences preferences;


    @Before
    public void setUp() {
        preferences = EasyMock.createNiceMock(Preferences.class);
        factory = new EntityManagerFactoryProvider(preferences);
    }


    /**
     * Tests that user settings are used. Connection always fails
     */
    @Test
    public void testGetFatory() {

        EasyMock.expect(preferences.getDBAddress()).andReturn("adress").once();
        EasyMock.expect(preferences.getPassword()).andReturn("pwd").once();
        EasyMock.expect(preferences.getUserName()).andReturn("user").once();
        EasyMock.replay(preferences);
        try {
            factory.getFatory();
        } catch (HibernateException e) {

            EasyMock.verify(preferences);
        }
    }

}
