package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.entity.Value;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.view.table.view.model.ViewTableModel;

import javax.persistence.Tuple;
import java.util.Date;


public class ModelBuilderVisitor implements Visitor<ViewTableModel> {

    private ViewTableModel model;
    private Tuple row;


    public ModelBuilderVisitor(Tuple row, ViewTableModel model) {
        super();
        this.row = row;
        this.model = model;
    }


    @Override
    public ViewTableModel accept(EndColumn column) {
        Time time = new Time();
        time.setId(row.get("id-time", Long.class));
        time.setEnd(row.get(column.getColumnName(), Date.class));
        model.setEnd(time);
        return model;
    }


    @Override
    public ViewTableModel accept(BeginColumn column) {
        Time time = new Time();
        time.setId(row.get("id-time", Long.class));
        time.setBegin(row.get(column.getColumnName(), Date.class));
        model.setBegin(time);
        return model;
    }


    @Override
    public ViewTableModel accept(DirectionColumn column) {
        Location location = new Location();
        location.setId(row.get("id-location", Long.class));
        location.setDirection(row.get(column.getColumnName(), String.class));
        model.setDirection(location);
        return model;
    }


    @Override
    public ViewTableModel accept(ValueColumn column) {
        Value value = new Value();
        value.setId(row.get("id-" + column.getColumnName(), Long.class));
        value.setValue(row.get(column.getColumnName(), String.class));
        model.setValue(value);
        return model;
    }


    @Override
    public ViewTableModel accept(PlaceColumn column) {
        Location location = new Location();
        location.setId(row.get("id-location", Long.class));
        location.setPlace(row.get(column.getColumnName(), String.class));
        model.setPlace(location);
        return model;
    }


    @Override
    public ViewTableModel accept(TypeColumn column) {
        Type type = new Type();
        type.setId(row.get("id-" + column.getColumnName(), Long.class));
        type.setType(row.get(column.getColumnName(), String.class));
        model.setType(type);
        return model;
    }

}
