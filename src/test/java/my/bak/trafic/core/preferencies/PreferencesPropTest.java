package my.bak.trafic.core.preferencies;

import jdk.nashorn.internal.ir.annotations.Ignore;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PreferencesPropTest {

    private Preferences pref;
    private Bus bus;


    @Before
    public void setUp() {
        LoggerTestInjector.inject(PreferencesProp.class);
        bus = EasyMock.createNiceMock(Bus.class);
        EasyMock.replay(bus);
        pref = new PreferencesProp(bus);
    }


    @Test
    public void testIfPrerenciesExist() {
        File file = new File(Preferences.PROP_FILE);
        String message = "File prefencies on path" + Preferences.PROP_FILE + "does not exist";
        assertTrue(message, file.exists());
    }


    @Test
    public void testGetPassword() {
        pref.setPassword("pwd");
        assertEquals("pwd", pref.getPassword());
    }


    @Test
    public void testGetDBAddress() {
        pref.setDBAdress("adress");
        assertEquals("adress", pref.getDBAddress());
    }


    @Test
    public void testGetUserName() {
        pref.setUserName("name");
        assertEquals("name", pref.getUserName());
    }


    @Ignore
    @Test
    public void testSave() {
        pref.setPassword("pwd");
        pref.setDBAdress("adress");
        pref.setUserName("name");
        pref.setStartConnected(true);
        pref.save();

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(Preferences.PROP_FILE)) {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("pwd", prop.getProperty("password"));
        assertEquals("adress", prop.getProperty("dbAdress"));
        assertEquals("name", prop.getProperty("userName"));
        assertEquals("true", prop.getProperty("connected"));

    }


    @Test
    public void testGetStartConnected() {
        pref.setStartConnected(true);
        assertEquals(true, pref.getStartConnected());
    }


    @After
    public void tearDown() {
        try {
            if (Files.deleteIfExists(new File(Preferences.PROP_FILE).toPath())) {
                System.out.println("Delete old preferencies");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
