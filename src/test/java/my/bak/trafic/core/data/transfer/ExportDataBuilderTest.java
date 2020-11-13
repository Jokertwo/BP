package my.bak.trafic.core.data.transfer;

import my.bak.trafic.core.plugin.transport.ExportDataBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;


public class ExportDataBuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ExportDataBuilder builder;


    @Before
    public void setUp() throws Exception {
        builder = new ExportDataBuilder();
    }


    @Test
    public void testBeginNotSet() {
        thrown.expect(NoSuchElementException.class);
        builder.getBegin().get();
    }


    @Test
    public void testEndNotSet() {
        thrown.expect(NoSuchElementException.class);
        builder.getEnd().get();
    }


    @Test
    public void testPlaceNotSet() {
        thrown.expect(NoSuchElementException.class);
        builder.getPlace().get();
    }


    @Test
    public void testDirectionNotSet() {
        thrown.expect(NoSuchElementException.class);
        builder.getDirection().get();
    }


    @Test
    public void testTypeNotSet() {
        thrown.expect(NoSuchElementException.class);
        builder.getType().get();
    }


    @Test
    public void testValueNotSet() {
        thrown.expect(NoSuchElementException.class);
        builder.getValue().get();
    }


    @Test
    public void testGetBegin() {
        Date date = new Date();
        builder.setBegin(date);
        assertEquals(date, builder.getBegin().get());
    }


    @Test
    public void testGetEnd() {
        Date date = new Date();
        builder.setEnd(date);
        assertEquals(date, builder.getEnd().get());
    }


    @Test
    public void testGetDirection() {
        builder.setDirection("txt");
        assertEquals("txt", builder.getDirection().get());
    }


    @Test
    public void testGetPlace() {
        builder.setPlace("txt");
        assertEquals("txt", builder.getPlace().get());
    }


    @Test
    public void testGetValue() {
        builder.setValue("txt");
        assertEquals("txt", builder.getValue().get());
    }


    @Test
    public void testGetType() {
        builder.setType("txt");
        assertEquals("txt", builder.getType().get());
    }

}
