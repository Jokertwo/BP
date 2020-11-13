package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.core.plugin.transport.ExportDataBuilder;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;

import javax.persistence.Tuple;
import java.util.Date;


public class DataBuilderVisitor implements Visitor<ExportDataBuilder> {
    private ExportDataBuilder dataBuilder;
    private Tuple row;


    public DataBuilderVisitor(Tuple row, ExportDataBuilder dataBuilder) {
        this.row = row;
        this.dataBuilder = dataBuilder;
    }


    @Override
    public ExportDataBuilder accept(EndColumn column) {
        return dataBuilder.setEnd(row.get(column.getColumnName(), Date.class));

    }


    @Override
    public ExportDataBuilder accept(BeginColumn column) {
        return dataBuilder.setBegin(row.get(column.getColumnName(), Date.class));
    }


    @Override
    public ExportDataBuilder accept(DirectionColumn column) {
        return dataBuilder.setDirection(row.get(column.getColumnName(), String.class));
    }


    @Override
    public ExportDataBuilder accept(ValueColumn column) {
        return dataBuilder.setValue(row.get(column.getColumnName(), String.class));
    }


    @Override
    public ExportDataBuilder accept(PlaceColumn column) {
        return dataBuilder.setPlace(row.get(column.getColumnName(), String.class));
    }


    @Override
    public ExportDataBuilder accept(TypeColumn column) {
        return dataBuilder.setType(row.get(column.getColumnName(), String.class));
    }

}
