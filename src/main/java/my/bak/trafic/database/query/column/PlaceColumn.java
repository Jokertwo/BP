package my.bak.trafic.database.query.column;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.query.visitor.Visitor;


public class PlaceColumn extends Column {

    public PlaceColumn() {
        super("place", Location.class);
    }


    @Override
    public <Type> Type visit(Visitor<Type> visitor) {
        return visitor.accept(this);
    }

}
