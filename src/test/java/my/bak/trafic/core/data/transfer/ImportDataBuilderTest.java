package my.bak.trafic.core.data.transfer;

import my.bak.trafic.core.plugin.transport.ImportDataBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ImportDataBuilderTest {

    private ImportDataBuilder builder;


    @Before
    public void setUp() {
        builder = new ImportDataBuilder();
    }


    @Test
    public void testGetBeginDate() {
        Date date = new Date();
        builder.setBeginDate(date);
        assertEquals(date, date);
    }


    @Test
    public void testnotSetBeginDate() {
        assertEquals(ImportDataBuilder.UNKNOWN_DATE, builder.getBeginDate());
    }


    @Test
    public void testGetEndDate() {
        Date date = new Date();
        builder.setEndDate(date);
        assertEquals(date, date);
    }


    @Test
    public void testNotSetEndDate() {
        assertEquals(ImportDataBuilder.UNKNOWN_DATE, builder.getEndDate());
    }


    @Test
    public void testGetLocation() {
        builder.setPlace("Praha");
        assertEquals("Praha", builder.getPlace());
    }


    @Test
    public void testNotSetPlace() {
        assertEquals(ImportDataBuilder.UNKNOWN_LOCATION, builder.getPlace());
    }


    @Test
    public void testGetDirection() {
        builder.setDirection("nevim");
        assertEquals("nevim", builder.getDirection());
    }


    @Test
    public void testNotSetDirection() {
        assertEquals(ImportDataBuilder.UNKNOWN_LOCATION, builder.getDirection());
    }


    @Test
    public void testGetData() {
        Map<String, String> map = new HashMap<>();
        map.put("rychlost", "1");
        builder.setData(map);
        assertEquals(map, builder.getData());
    }


    @Test
    public void testNotSetData() {
        assertNotNull(builder.getData());
        assertEquals(true, builder.getData().isEmpty());
    }

}
