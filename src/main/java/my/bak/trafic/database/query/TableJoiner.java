package my.bak.trafic.database.query;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.entity.Value;

import javax.persistence.criteria.Path;


public interface TableJoiner {

    Path<Time> getTimePath();


    Path<Location> getLocationPath();


    Path<Value> getValuePath();


    Path<Type> getTypePath();
}
