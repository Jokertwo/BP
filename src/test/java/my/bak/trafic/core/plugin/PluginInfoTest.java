package my.bak.trafic.core.plugin;

import my.bak.trafic.core.plugin.loader.PluginInfo;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;


public class PluginInfoTest {
    private PluginInfo pluginInfo;
    private ImportPlugin plugin;
    private File file;


    @Before
    public void setUp() throws Exception {

        file = new File("some/path");

        plugin = EasyMock.createNiceMock(ImportPlugin.class);

        EasyMock.expect(plugin.getName()).andReturn("testPlugin");
        EasyMock.expect(plugin.getDescription()).andReturn("testDescription");
        EasyMock.expect(plugin.getPluginFlag()).andReturn(PluginFlag.IMPORT);
        EasyMock.replay(plugin);

        pluginInfo = new PluginInfo(plugin, file);
    }


    @Test
    public void testGetPluginFile() {
        assertEquals(file.getAbsolutePath(), pluginInfo.getPluginPath());
        EasyMock.verify(plugin);
    }


    @Test
    public void testGetName() {
        assertEquals("testPlugin", pluginInfo.getName());
        EasyMock.verify(plugin);
    }


    @Test
    public void testGetDescription() {
        assertEquals("testDescription", pluginInfo.getDescription());
        EasyMock.verify(plugin);
    }


    @Test
    public void testGetSupportedOperation() {
        assertEquals(PluginFlag.IMPORT, pluginInfo.getFlag());
        EasyMock.verify(plugin);
    }

}
