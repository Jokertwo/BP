package my.bak.trafic.database.query.visitor.impl;

import javafx.scene.control.TextField;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.test.hepler.JfxTestRunner;
import my.bak.trafic.view.NodeWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tornadofx.control.DateTimePicker;

import static org.junit.Assert.assertTrue;


@RunWith(JfxTestRunner.class)
public class ValueEditorVisitorTest {
    private Visitor<NodeWrapper> visitor;
    private NodeWrapper wrapper;


    @Before
    public void setUp() throws Exception {
        visitor = new ValueEditorVisitor();
    }


    @Test
    public void testAcceptEndColumn() {
        wrapper = new EndColumn().visit(visitor);
        assertTrue(wrapper.getNode() instanceof DateTimePicker);
    }


    @Test
    public void testAcceptBeginColumn() {
        wrapper = new BeginColumn().visit(visitor);
        assertTrue(wrapper.getNode() instanceof DateTimePicker);
    }


    @Test
    public void testAcceptDirectionColumn() {
        wrapper = new DirectionColumn().visit(visitor);
        assertTrue(wrapper.getNode() instanceof TextField);
    }


    @Test
    public void testAcceptValueColumn() {
        wrapper = new ValueColumn().visit(visitor);
        assertTrue(wrapper.getNode() instanceof TextField);
    }


    @Test
    public void testAcceptPlaceColumn() {
        wrapper = new PlaceColumn().visit(visitor);
        assertTrue(wrapper.getNode() instanceof TextField);
    }


    @Test
    public void testAcceptTypeColumn() {
        wrapper = new TypeColumn().visit(visitor);
        assertTrue(wrapper.getNode() instanceof TextField);
    }

}
