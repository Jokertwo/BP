package my.bak.trafic.database.query.visitor;

public interface WhereVisitor<Type> {

    Type equals();


    Type greaterThan();


    Type lessThen();


    Type greaterThanOrEquals();


    Type lessThanOrEquals();


    Type notEquals();

}
