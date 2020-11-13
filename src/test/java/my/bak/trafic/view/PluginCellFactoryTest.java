package my.bak.trafic.view;

import my.bak.trafic.core.plugin.ImportPlugin;
import my.bak.trafic.core.plugin.PluginFlag;
import my.bak.trafic.core.plugin.loader.PluginInfo;
import my.bak.trafic.test.hepler.JfxTestRunner;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertEquals;


@RunWith(JfxTestRunner.class)
public class PluginCellFactoryTest {

    private PluginCellFactory factory;
    private ImportPlugin plugin;


    @Before
    public void setUp() throws Exception {
        factory = new PluginCellFactory();

        plugin = EasyMock.createNiceMock(ImportPlugin.class);

        EasyMock.expect(plugin.getName()).andReturn("testPlugin").once();
        EasyMock.expect(plugin.getDescription()).andReturn("testDescription").once();
        EasyMock.expect(plugin.getPluginFlag()).andReturn(PluginFlag.IMPORT).once();
        EasyMock.replay(plugin);
    }


    @Test
    public void testUpdateItemPluginInfoBoolean() {
        File file = new File("is/not/nesseary");
        PluginInfo info = new PluginInfo(plugin, file);

        factory.updateItem(info, false);

        assertEquals("testPlugin" + " " + "(" + PluginFlag.IMPORT.name() + ")", factory.getText());

        EasyMock.verify(plugin);
    }


    @Test
    public void testUpdateItemPluginNull() {
        factory.updateItem(null, true);
        assertEquals(null, factory.getText());
    }

}
