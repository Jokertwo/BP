package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.database.query.column.*;
import my.bak.trafic.view.table.view.model.ViewTableModel;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Tuple;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class ModelBuilderVisitorTest {
    private ViewTableModel model;
    private Tuple row;


    @Before
    public void setUp() throws Exception {
        model = new ViewTableModel();
        row = EasyMock.createNiceMock(Tuple.class);
    }


    @Test
    public void testAcceptEndColumn() {
        EndColumn column = new EndColumn();
        Date date = new Date();
        EasyMock.expect(row.get("id-time", Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), Date.class)).andReturn(date).once();
        EasyMock.replay(row);
        model = new ModelBuilderVisitor(row, model).accept(column);
        assertEquals(1L, model.getEnd().getId());
        assertEquals(date, model.getEnd().getEnd());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptBeginColumn() {
        BeginColumn column = new BeginColumn();
        Date date = new Date();
        EasyMock.expect(row.get("id-time", Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), Date.class)).andReturn(date).once();
        EasyMock.replay(row);
        model = new ModelBuilderVisitor(row, model).accept(column);
        assertEquals(1L, model.getBegin().getId());
        assertEquals(date, model.getBegin().getBegin());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptDirectionColumn() {
        DirectionColumn column = new DirectionColumn();
        EasyMock.expect(row.get("id-location", Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("abc").once();
        EasyMock.replay(row);
        model = new ModelBuilderVisitor(row, model).accept(column);
        assertEquals(1L, model.getDirection().getId());
        assertEquals("abc", model.getDirection().getDirection());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptValueColumn() {
        ValueColumn column = new ValueColumn();
        EasyMock.expect(row.get("id-" + column.getColumnName(), Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("abc").once();
        EasyMock.replay(row);
        model = new ModelBuilderVisitor(row, model).accept(column);
        assertEquals(1L, model.getValue().getId());
        assertEquals("abc", model.getValue().getValue());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptPlaceColumn() {
        PlaceColumn column = new PlaceColumn();
        EasyMock.expect(row.get("id-location", Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("abc").once();
        EasyMock.replay(row);
        model = new ModelBuilderVisitor(row, model).accept(column);
        assertEquals(1L, model.getPlace().getId());
        assertEquals("abc", model.getPlace().getPlace());
        EasyMock.verify(row);

    }


    @Test
    public void testAcceptTypeColumn() {
        TypeColumn column = new TypeColumn();
        EasyMock.expect(row.get("id-" + column.getColumnName(), Long.class)).andReturn(1L).once();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("abc").once();
        EasyMock.replay(row);
        model = new ModelBuilderVisitor(row, model).accept(column);
        assertEquals(1L, model.getType().getId());
        assertEquals("abc", model.getType().getType());
        EasyMock.verify(row);
    }

}
