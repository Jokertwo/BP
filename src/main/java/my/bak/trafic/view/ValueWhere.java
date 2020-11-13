package my.bak.trafic.view;

import my.bak.trafic.database.query.column.Column;


public interface ValueWhere {

    /**
     * Get current value
     *
     * @return
     */
    Object getValue();


    /**
     * Change editor based on type of column
     *
     * @param column
     */
    void changeEditor(Column column);
}
