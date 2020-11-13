package my.bak.trafic.view.clipboard;

import javafx.scene.control.TreeItem;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.test.hepler.JfxTestRunner;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import my.bak.trafic.view.table.where.model.WhereTableModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(JfxTestRunner.class)
public class ClipboardImplTest {
    private Clipboard clipboard;
    private List<Column> columnList;
    private TreeItem<WhereTableModel> root;


    @BeforeClass
    public static void tearUp() {
        LoggerTestInjector.inject(ClipboardImpl.class);
    }


    @Before
    public void setUp() {
        clipboard = new ClipboardImpl();
        columnList = new ArrayList<>();
        root = new TreeItem<>();
    }


    @Test
    public void testIsEmpty() {
        assertTrue(clipboard.isEmpty().get());
    }


    @Test
    public void testIsNotEmpty() {
        clipboard.store(columnList, root, false);
        assertFalse(clipboard.isEmpty().get());
    }


    @Test
    public void testGetRoot() {
        clipboard.store(columnList, root, false);
        assertEquals(root.getChildren(), clipboard.getRoot().getChildren());
    }


    @Test
    public void testGetColumns() {
        clipboard.store(columnList, root, false);
        assertEquals(columnList, clipboard.getColumns());
    }


    public void testGetDiscting() {
        clipboard.store(columnList, root, false);
        assertEquals(false, clipboard.getDistinct());
    }

}
