package my.bak.trafic.database.query.column;

import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.query.visitor.Visitor;


public class BeginColumn extends Column {

    public BeginColumn() {
        super("begin", Time.class);
    }


    @Override
    public <Type> Type visit(Visitor<Type> visitor) {
        return visitor.accept(this);
    }

}
