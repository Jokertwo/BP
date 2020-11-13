package my.bak.trafic.database.query;

import javafx.scene.control.TreeItem;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.view.table.where.model.WhereTableModel;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public interface Query {

    /**
     * Build select query
     *
     * @param columns
     * @param root
     * @param distinct
     * @return
     */
    CriteriaQuery<Tuple> createSelectQuery(List<Column> columns,
                                           TreeItem<WhereTableModel> root,
                                           boolean distinct);


    /**
     * Execute query
     *
     * @param query
     * @return
     */
    List<Tuple> executeSelectQuery(CriteriaQuery<Tuple> query);


    /**
     * Return all available columns
     *
     * @return
     */
    List<Column> getAllColumns();


    /**
     * Return count of row for current query
     *
     * @param query
     * @return
     */
    long getCount(CriteriaQuery<Tuple> query);
}
