package my.bak.trafic.database.query.column;

import my.bak.trafic.database.entity.Value;
import my.bak.trafic.database.query.visitor.Visitor;


public class ValueColumn extends Column {

    public ValueColumn() {
        super("value", Value.class);
    }


    @Override
    public <Type> Type visit(Visitor<Type> visitor) {
        return visitor.accept(this);
    }

}
