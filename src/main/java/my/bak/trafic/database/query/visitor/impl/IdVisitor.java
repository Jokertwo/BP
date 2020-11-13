package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.database.entity.*;
import my.bak.trafic.database.query.TableJoiner;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import java.util.Optional;


public class IdVisitor implements Visitor<Optional<Selection<Long>>> {

    private Path<Value> qr;
    private Path<Time> time;
    private Path<Location> location;
    private Path<Type> type;

    private boolean isTimeIdSet = false;
    private boolean isLocaitonIdSet = false;


    public IdVisitor(TableJoiner joiner) {
        super();
        this.qr = joiner.getValuePath();
        this.time = joiner.getTimePath();
        this.location = joiner.getLocationPath();
        this.type = joiner.getTypePath();
    }


    @Override
    public Optional<Selection<Long>> accept(EndColumn column) {
        if (!isTimeIdSet) {
            isTimeIdSet = true;
            return Optional.of(time.get(Time_.id).alias("id-time"));
        }
        return Optional.empty();
    }


    @Override
    public Optional<Selection<Long>> accept(BeginColumn column) {
        if (!isTimeIdSet) {
            isTimeIdSet = true;
            return Optional.of(time.get(Time_.id).alias("id-time"));
        }
        return Optional.empty();
    }


    @Override
    public Optional<Selection<Long>> accept(DirectionColumn column) {
        if (!isLocaitonIdSet) {
            isLocaitonIdSet = true;
            return Optional.of(location.get(Location_.id).alias("id-location"));
        }
        return Optional.empty();

    }


    @Override
    public Optional<Selection<Long>> accept(ValueColumn column) {
        return Optional.of(qr.get(Value_.id).alias("id-" + column.getColumnName()));
    }


    @Override
    public Optional<Selection<Long>> accept(PlaceColumn column) {
        if (!isLocaitonIdSet) {
            isLocaitonIdSet = true;
            return Optional.of(location.get(Location_.id).alias("id-location"));
        }
        return Optional.empty();
    }


    @Override
    public Optional<Selection<Long>> accept(TypeColumn column) {
        return Optional.of(type.get(Type_.id).alias("id-" + column.getColumnName()));
    }

}
