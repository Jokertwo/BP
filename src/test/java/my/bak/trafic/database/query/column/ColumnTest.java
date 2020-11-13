package my.bak.trafic.database.query.column;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.entity.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColumnTest {

    private Column column;

    @Test
    public void testBeginColumn() {
        column = new BeginColumn();
        assertEquals("begin", column.getColumnName());
        assertEquals(Time.class, column.getClazz());
    }

    @Test
    public void testEndColumn() {
        column = new EndColumn();
        assertEquals("end", column.getColumnName());
        assertEquals(Time.class, column.getClazz());
    }

    @Test
    public void testPlaceColumn() {
        column = new PlaceColumn();
        assertEquals("place", column.getColumnName());
        assertEquals(Location.class, column.getClazz());
    }

    @Test
    public void testDirectionColumn() {
        column = new DirectionColumn();
        assertEquals("direction", column.getColumnName());
        assertEquals(Location.class, column.getClazz());
    }

    @Test
    public void testValueColumn() {
        column = new ValueColumn();
        assertEquals("value", column.getColumnName());
        assertEquals(Value.class, column.getClazz());
    }

    @Test
    public void testTypeColumn() {
        column = new TypeColumn();
        assertEquals("type", column.getColumnName());
        assertEquals(Type.class, column.getClazz());
    }

}