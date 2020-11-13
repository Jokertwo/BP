package my.bak.trafic.view.clipboard;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TreeItem;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.view.table.where.model.WhereTableModel;

import java.util.List;


public interface Clipboard {

    /**
     * Store query to clipboard
     *
     * @param columns  columns defined by user
     * @param root     root node of where table
     * @param distinct if user want to use distinct
     */
    void store(List<Column> columns, TreeItem<WhereTableModel> root, boolean distinct);


    /**
     * Inform about if in clipboard some values
     *
     * @return return true if clipboard is empty
     */
    BooleanProperty isEmpty();


    /**
     * Root of where table with condition defined by user
     *
     * @return Return root node of tree table
     */
    TreeItem<WhereTableModel> getRoot();


    /**
     * Collection of columns defined by user
     *
     * @return Return collection of columns defined by user
     */
    List<Column> getColumns();


    /**
     * Return true is should be use distinct function
     *
     * @return Return true is should be use distinct function
     */
    boolean getDistinct();

}
