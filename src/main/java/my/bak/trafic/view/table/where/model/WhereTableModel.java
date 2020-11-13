package my.bak.trafic.view.table.where.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import my.bak.trafic.database.query.QueryCombination;
import my.bak.trafic.database.query.QueryOperator;
import my.bak.trafic.database.query.column.Column;


public class WhereTableModel {
    @Getter
    private final ObjectProperty<Column> columnProperty;
    @Getter
    private final ObjectProperty<QueryOperator> operatorProperty;
    @Getter
    private final ObjectProperty<Object> valueProperty;
    @Getter
    private final ObjectProperty<QueryCombination> combinationProperty;
    @Getter
    private boolean isContainer = false;


    public WhereTableModel() {
        columnProperty = new SimpleObjectProperty<>();
        operatorProperty = new SimpleObjectProperty<>();
        valueProperty = new SimpleObjectProperty<>();
        combinationProperty = new SimpleObjectProperty<>();
    }


    public void setColumn(Column column) {
        this.columnProperty.set(column);
    }


    public void setOperator(QueryOperator operator) {
        this.operatorProperty.set(operator);
    }


    public void setValue(Object value) {
        this.valueProperty.set(value);
    }


    public void setCombination(QueryCombination combination) {
        isContainer = true;
        this.combinationProperty.set(combination);
    }


    public Column getColumn() {
        return columnProperty.get();
    }


    public QueryOperator getOperator() {
        return operatorProperty.get();
    }


    public Object getValue() {
        return valueProperty.get();
    }


    public QueryCombination getCombination() {
        return combinationProperty.get();
    }

}
