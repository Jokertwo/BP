package my.bak.trafic.database.query.column;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.query.visitor.Visitor;


public class DirectionColumn extends Column {

    public DirectionColumn() {
        super("direction", Location.class);
    }


    @Override
    public <Type> Type visit(Visitor<Type> visitor) {
        return visitor.accept(this);
    }

}
