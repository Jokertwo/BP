package my.bak.trafic.database.query.visitor.impl;

import javafx.scene.control.TableColumn;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.view.table.view.model.ViewTableModel;

import java.util.Date;


public class ColumnBuilderVisitor implements Visitor<TableColumn<ViewTableModel, ?>> {

    @Override
    public TableColumn<ViewTableModel, Date> accept(EndColumn column) {
        TableColumn<ViewTableModel, Date> tableColumn = new TableColumn<>(column.getColumnName());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
        return tableColumn;
    }


    @Override
    public TableColumn<ViewTableModel, Date> accept(BeginColumn column) {
        TableColumn<ViewTableModel, Date> tableColumn = new TableColumn<>(column.getColumnName());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getBeginProperty());
        return tableColumn;
    }


    @Override
    public TableColumn<ViewTableModel, String> accept(DirectionColumn column) {
        TableColumn<ViewTableModel, String> tableColumn = new TableColumn<>(column.getColumnName());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getDirectionProperty());
        return tableColumn;
    }


    @Override
    public TableColumn<ViewTableModel, String> accept(ValueColumn column) {
        TableColumn<ViewTableModel, String> tableColumn = new TableColumn<>(column.getColumnName());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
        return tableColumn;
    }


    @Override
    public TableColumn<ViewTableModel, String> accept(PlaceColumn column) {
        TableColumn<ViewTableModel, String> tableColumn = new TableColumn<>(column.getColumnName());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getPlaceProperty());
        return tableColumn;
    }


    @Override
    public TableColumn<ViewTableModel, String> accept(TypeColumn column) {
        TableColumn<ViewTableModel, String> tableColumn = new TableColumn<>(column.getColumnName());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        return tableColumn;
    }

}
