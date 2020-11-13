package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.core.plugin.transport.ExportDataBuilder;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Tuple;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class DataBuilderVisitorTest {

    private Visitor<ExportDataBuilder> visitor;
    private ExportDataBuilder builder;
    private Tuple row;
    private Column column;


    @Before
    public void setUp() {
        builder = new ExportDataBuilder();
        row = EasyMock.createNiceMock(Tuple.class);
        visitor = new DataBuilderVisitor(row, builder);
    }


    @Test
    public void testAcceptEndColumn() {
        Date date = new Date();
        column = new EndColumn();
        EasyMock.expect(row.get(column.getColumnName(), Date.class)).andReturn(date).once();
        EasyMock.replay(row);
        builder = column.visit(visitor);
        assertEquals(date, builder.getEnd().get());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptBeginColumn() {
        Date date = new Date();
        column = new BeginColumn();
        EasyMock.expect(row.get(column.getColumnName(), Date.class)).andReturn(date).once();
        EasyMock.replay(row);
        builder = column.visit(visitor);
        assertEquals(date, builder.getBegin().get());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptDirectionColumn() {
        column = new DirectionColumn();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("txt").once();
        EasyMock.replay(row);
        builder = column.visit(visitor);
        assertEquals("txt", builder.getDirection().get());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptValueColumn() {
        column = new ValueColumn();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("txt").once();
        EasyMock.replay(row);
        builder = column.visit(visitor);
        assertEquals("txt", builder.getValue().get());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptPlaceColumn() {
        column = new PlaceColumn();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("txt").once();
        EasyMock.replay(row);
        builder = column.visit(visitor);
        assertEquals("txt", builder.getPlace().get());
        EasyMock.verify(row);
    }


    @Test
    public void testAcceptTypeColumn() {
        column = new TypeColumn();
        EasyMock.expect(row.get(column.getColumnName(), String.class)).andReturn("txt").once();
        EasyMock.replay(row);
        builder = column.visit(visitor);
        assertEquals("txt", builder.getType().get());
        EasyMock.verify(row);
    }

}
