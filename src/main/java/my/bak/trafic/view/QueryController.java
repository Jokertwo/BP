package my.bak.trafic.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.view.table.where.model.WhereTableModel;


public interface QueryController {

    /**
     * Component where user can define database query
     *
     * @return {@link Node} with component
     */
    Node getContent();


    /**
     * Collection of {@link Column} which user want to see
     *
     * @return return {@link ObservableList}, can be empty
     */
    ObservableList<Column> getColumns();


    /**
     * Represent root node with with conditions defined by user. It is definition of WHERE part of sql query
     *
     * @return return {@link TreeItem} with model {@link WhereTableModel}
     */
    TreeItem<WhereTableModel> getRoot();


    /**
     * Return true is user want to distinct function
     *
     * @return Return true if should be distinct query
     */
    boolean isDistinct();


    /**
     * Set distinct checkbox
     *
     * @param distinct true if should be set
     */
    void setDistinct(boolean distinct);
}
