package my.bak.trafic.database.query.visitor.impl;

import javafx.scene.control.TableColumn;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.view.table.view.model.ViewTableModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ColumnBuilderVisitorTest {
    private Visitor<TableColumn<ViewTableModel, ?>> visitor;


    @Before
    public void setUp() throws Exception {
        visitor = new ColumnBuilderVisitor();
    }


    @Test
    public void testAcceptEndColumn() {
        TableColumn<ViewTableModel, ?> end = visitor.accept(new EndColumn());
        assertNotNull(end);
    }


    @Test
    public void testAcceptBeginColumn() {
        TableColumn<ViewTableModel, ?> end = visitor.accept(new BeginColumn());
        assertNotNull(end);
    }


    @Test
    public void testAcceptDirectionColumn() {
        TableColumn<ViewTableModel, ?> end = visitor.accept(new DirectionColumn());
        assertNotNull(end);
    }


    @Test
    public void testAcceptValueColumn() {
        TableColumn<ViewTableModel, ?> end = visitor.accept(new ValueColumn());
        assertNotNull(end);
    }


    @Test
    public void testAcceptPlaceColumn() {
        TableColumn<ViewTableModel, ?> end = visitor.accept(new PlaceColumn());
        assertNotNull(end);
    }


    @Test
    public void testAcceptTypeColumn() {
        TableColumn<ViewTableModel, ?> end = visitor.accept(new TypeColumn());
        assertNotNull(end);
    }

}
