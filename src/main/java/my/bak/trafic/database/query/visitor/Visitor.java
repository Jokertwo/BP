package my.bak.trafic.database.query.visitor;

import my.bak.trafic.database.query.column.*;

public interface Visitor<Type> {

    Type accept(EndColumn column);


    Type accept(BeginColumn column);


    Type accept(DirectionColumn column);


    Type accept(ValueColumn column);


    Type accept(PlaceColumn column);


    Type accept(TypeColumn column);

}
