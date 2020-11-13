package my.bak.trafic.view.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TitledPane;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.column.EndColumn;
import my.bak.trafic.view.table.view.model.ViewTableModel;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ViewUtilImplTest {
    private ViewUtil util;
    private Tuple row;


    @Before
    public void setUp() throws Exception {
        util = new ViewUtilImpl();
        row = EasyMock.createNiceMock(Tuple.class);
    }


    @Test
    public void testEmptyColumns() {
        ViewTableModel empty = new ViewTableModel();
        ViewTableModel created = util.createViewTableModel(FXCollections.emptyObservableList(), null);
        assertEquals(empty.getBegin(), created.getBegin());
        assertEquals(empty.getEnd(), created.getEnd());
        assertEquals(empty.getDirection(), created.getDirection());
        assertEquals(empty.getPlace(), created.getPlace());
        assertEquals(empty.getValue(), created.getValue());
        assertEquals(empty.getType(), created.getType());
    }


    @Test
    public void testCreateViewTableModel() {
        EndColumn column = new EndColumn();
        ObservableList<Column> columns = FXCollections.observableArrayList(column);
        Date date = new Date();
        EasyMock.expect(row.get("id-time", Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), Date.class)).andReturn(date).once();
        EasyMock.replay(row);

        ViewTableModel created = util.createViewTableModel(columns, row);
        assertEquals(date, created.getEnd().getEnd());
        assertEquals(1L, created.getEnd().getId());

        EasyMock.verify(row);

    }


    @Test
    public void testCreateStaceTrace() {
        Optional<TitledPane> node = util.createStaceTrace(new IllegalStateException());
        assertTrue(node.isPresent());
    }


    @Test
    public void testNullCreateStaceTrace() {
        assertTrue(!util.createStaceTrace(null).isPresent());
    }

}
