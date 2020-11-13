package my.bak.trafic.database.query.visitor.impl;

import lombok.Getter;
import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.entity.Value;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;


public class RootVisitor implements Visitor<Void> {
    @Getter
    private Path<Time> timePath;
    @Getter
    private Path<Location> locationPath;
    @Getter
    private Path<Value> valuePath;
    @Getter
    private Path<Type> typePath;
    private CriteriaQuery<Tuple> q;


    public RootVisitor(CriteriaQuery<Tuple> q) {
        super();
        this.q = q;
    }


    @Override
    public Void accept(EndColumn column) {
        timePath = q.from(Time.class);
        return null;
    }


    @Override
    public Void accept(BeginColumn column) {
        timePath = q.from(Time.class);
        return null;
    }


    @Override
    public Void accept(DirectionColumn column) {
        locationPath = q.from(Location.class);
        return null;
    }


    @Override
    public Void accept(ValueColumn column) {
        valuePath = q.from(Value.class);
        return null;
    }


    @Override
    public Void accept(PlaceColumn column) {
        locationPath = q.from(Location.class);
        return null;
    }


    @Override
    public Void accept(TypeColumn column) {
        typePath = q.from(Type.class);
        return null;
    }

}
