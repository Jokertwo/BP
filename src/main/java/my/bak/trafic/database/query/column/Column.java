package my.bak.trafic.database.query.column;

import lombok.Getter;
import my.bak.trafic.database.query.visitor.Visitor;


public abstract class Column {
    @Getter
    private final String columnName;
    @Getter
    private final String tableName;
    @Getter
    private final Class<?> clazz;


    @Override
    public String toString() {
        return columnName + " (" + tableName + ")";
    }


    Column(String columnName, Class<?> tableName) {
        super();
        this.columnName = columnName;
        this.clazz = tableName;
        this.tableName = tableName.getName().substring(tableName.getName().lastIndexOf(".") + 1);
    }


    public abstract <Type> Type visit(Visitor<Type> visitor);

}
