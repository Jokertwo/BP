package my.bak.trafic.view.util;

import javafx.scene.control.TitledPane;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.view.table.view.model.ViewTableModel;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;


/**
 * Helper class for view
 *
 * @author Petr Lastovka
 */
public interface ViewUtil {

    /**
     * Create model for view table
     *
     * @param columns
     * @param row
     * @return
     */
    ViewTableModel createViewTableModel(List<Column> columns, Tuple row);


    /**
     * Create textarea with stack trace of throwable
     *
     * @param e any exception
     * @return return Node with text area
     */
    Optional<TitledPane> createStaceTrace(Throwable e);
}
