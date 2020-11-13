package my.bak.trafic.view;

import javafx.scene.control.TextField;
import my.bak.trafic.test.hepler.JfxTestRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;


@RunWith(JfxTestRunner.class)
public class NodeWrapperTest {
    private NodeWrapper wrapper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testNodeWrapperGetNodeTextField() {
        TextField tf = new TextField();
        wrapper = new NodeWrapper(tf);
        assertEquals(tf, wrapper.getNode());
    }


    @Test
    public void testNodeWrapperGetNodeDateTimePicker() {
        DateTimePicker dp = new DateTimePicker();
        wrapper = new NodeWrapper(dp);
        assertEquals(dp, wrapper.getNode());
    }


    @Test
    public void testNodeWrapperNullDateTimePicker() {
        DateTimePicker dp = null;
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Can not set null Node to NodeWrapper");
        wrapper = new NodeWrapper(dp);
    }


    @Test
    public void testNodeWrapperNullTextField() {
        TextField tf = null;
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Can not set null Node to NodeWrapper");
        wrapper = new NodeWrapper(tf);
    }


    @Test
    public void testGetValueTextField() {
        TextField tf = new TextField();
        tf.setText("txt");
        wrapper = new NodeWrapper(tf);
        assertEquals("txt", wrapper.getValue());
    }


    @Test
    public void testGetValueDateTimePicker() {
        Date date = new Date(0);
        DateTimePicker picker = new DateTimePicker();
        picker.setDateTimeValue(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        wrapper = new NodeWrapper(picker);
        assertEquals(date, wrapper.getValue());

    }


    @Test
    public void testGetEmptyValueTextField() {
        TextField tf = new TextField();
        wrapper = new NodeWrapper(tf);
        assertEquals("", wrapper.getValue());
    }

}
