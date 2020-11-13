package my.bak.trafic.database.query.column;

import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.query.visitor.Visitor;


public class EndColumn extends Column {

    public EndColumn() {
        super("end", Time.class);
    }


    @Override
    public <Type> Type visit(Visitor<Type> visitor) {
        return visitor.accept(this);
    }

}
