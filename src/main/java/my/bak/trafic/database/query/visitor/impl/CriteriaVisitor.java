package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.database.entity.*;
import my.bak.trafic.database.query.TableJoiner;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import java.util.Date;


public class CriteriaVisitor implements Visitor<Selection<?>> {

    private Path<Value> qr;
    private Path<Time> time;
    private Path<Location> location;
    private Path<Type> type;


    public CriteriaVisitor(TableJoiner joiner) {
        super();
        this.qr = joiner.getValuePath();
        this.time = joiner.getTimePath();
        this.location = joiner.getLocationPath();
        this.type = joiner.getTypePath();
    }


    @Override
    public Selection<Date> accept(EndColumn column) {
        return time.get(Time_.end).alias(column.getColumnName());
    }


    @Override
    public Selection<Date> accept(BeginColumn column) {
        return time.get(Time_.begin).alias(column.getColumnName());
    }


    @Override
    public Selection<String> accept(DirectionColumn column) {
        return location.get(Location_.direction).alias(column.getColumnName());
    }


    @Override
    public Selection<String> accept(ValueColumn column) {
        return qr.get(Value_.value).alias(column.getColumnName());
    }


    @Override
    public Selection<String> accept(PlaceColumn column) {
        return location.get(Location_.place).alias(column.getColumnName());
    }


    @Override
    public Selection<String> accept(TypeColumn column) {
        return type.get(Type_.type).alias(column.getColumnName());
    }

}
