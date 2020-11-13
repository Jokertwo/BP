package my.bak.trafic.view.table.view.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.entity.Value;

import java.util.Date;


public class ViewTableModel {
    // time table
    @Getter
    private final ObjectProperty<Date> beginProperty;
    @Getter
    private Time begin;
    @Getter
    private final ObjectProperty<Date> endProperty;
    @Getter
    private Time end;

    // location table
    @Getter
    private final StringProperty placeProperty;
    @Getter
    private Location place;
    @Getter
    private final StringProperty directionProperty;
    @Getter
    private Location direction;
    // type table
    @Getter
    private final StringProperty typeProperty;
    @Getter
    private Type type;
    // value table
    @Getter
    private final StringProperty valueProperty;
    @Getter
    private Value value;


    public ViewTableModel() {
        beginProperty = new SimpleObjectProperty<>();
        endProperty = new SimpleObjectProperty<>();
        placeProperty = new SimpleStringProperty();
        directionProperty = new SimpleStringProperty();
        typeProperty = new SimpleStringProperty();
        valueProperty = new SimpleStringProperty();
    }


    public void setBegin(Time begin) {
        this.begin = begin;
        beginProperty.set(begin.getBegin());
    }


    public void setEnd(Time end) {
        this.end = end;
        endProperty.set(end.getEnd());
    }


    public void setPlace(Location place) {
        this.place = place;
        placeProperty.set(place.getPlace());
    }


    public void setDirection(Location direction) {
        this.direction = direction;
        directionProperty.set(direction.getDirection());
    }


    public void setType(Type type) {
        this.type = type;
        typeProperty.set(type.getType());
    }


    public void setValue(Value value) {
        this.value = value;
        valueProperty.set(value.getValue());
    }

}
