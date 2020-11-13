package my.bak.trafic.view.table.view.model;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.entity.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;


public class ViewTableModelTest {
    private ViewTableModel model;
    private Time time;
    private Value value;
    private Location location;
    private Type type;


    @Before
    public void setUp() throws Exception {
        model = new ViewTableModel();
        Date date = new Date();

        time = new Time();
        time.setBegin(date);
        time.setEnd(date);


        value = new Value();
        value.setValue("value");

        location = new Location();
        location.setDirection("dir");
        location.setPlace("place");

        type = new Type();
        type.setType("type");
    }


    @Test
    public void testGetBeginProperty() {
        model.setBegin(time);
        assertEquals(time.getBegin(), model.getBeginProperty().getValue());
    }


    @Test
    public void testGetBegin() {
        model.setBegin(time);
        assertEquals(time, model.getBegin());
    }


    @Test
    public void testGetEndProperty() {
        model.setEnd(time);
        assertEquals(time.getEnd(), model.getEndProperty().getValue());
    }


    @Test
    public void testGetEnd() {
        model.setEnd(time);
        assertEquals(time, model.getEnd());
    }


    @Test
    public void testGetPlaceProperty() {
        model.setPlace(location);
        assertEquals(location.getPlace(), model.getPlaceProperty().getValue());
    }


    @Test
    public void testGetPlace() {
        model.setPlace(location);
        assertEquals(location, model.getPlace());
    }


    @Test
    public void testGetDirectionProperty() {
        model.setDirection(location);
        assertEquals(location.getDirection(), model.getDirectionProperty().getValue());
    }


    @Test
    public void testGetDirection() {
        model.setDirection(location);
        assertEquals(location, model.getDirection());
    }


    @Test
    public void testGetTypeProperty() {
        model.setType(type);
        assertEquals(type.getType(), model.getTypeProperty().getValue());
    }


    @Test
    public void testGetType() {
        model.setType(type);
        assertEquals(type, model.getType());
    }


    @Test
    public void testGetValueProperty() {
        model.setValue(value);
        assertEquals(value.getValue(), model.getValueProperty().getValue());
    }


    @Test
    public void testGetValue() {
        model.setValue(value);
        assertEquals(value, model.getValue());
    }

}
