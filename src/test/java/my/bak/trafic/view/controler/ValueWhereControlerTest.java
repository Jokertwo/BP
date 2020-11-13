package my.bak.trafic.view.controler;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import my.bak.trafic.database.query.column.EndColumn;
import my.bak.trafic.test.hepler.JfxTestRunner;
import my.bak.trafic.test.hepler.LoggerTestInjector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import tornadofx.control.DateTimePicker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JfxTestRunner.class)
public class ValueWhereControlerTest {

    private ValueWhereControler controler;
    private HBox box;


    @BeforeClass
    public static void tearUp() {
        LoggerTestInjector.inject(ValueWhereControler.class);
    }


    @Before
    public void setUp() throws Exception {
        box = new HBox();
        controler = new ValueWhereControler(box);
    }


    @Test
    public void testValueWhereControler() {
        assertEquals(1, box.getChildren().size());
        assertTrue(box.getChildren().get(0) instanceof TextField);
    }


    @Test
    public void testGetValue() {
        TextField tf = (TextField) box.getChildren().get(0);
        tf.setText("txt");
        assertEquals("txt", controler.getValue());
    }


    @Test
    public void testChangeEditor() {
        assertTrue(box.getChildren().get(0) instanceof TextField);
        controler.changeEditor(new EndColumn());
        assertEquals(1, box.getChildren().size());
        assertTrue(box.getChildren().get(0) instanceof DateTimePicker);
    }

}
