package my.bak.trafic.database.query.column;

import my.bak.trafic.database.query.visitor.Visitor;

public class TypeColumn extends Column {

    public TypeColumn() {
        super("type", my.bak.trafic.database.entity.Type.class);
    }


    @Override
    public <Type> Type visit(Visitor<Type> visitor) {
        return visitor.accept(this);
    }

}
