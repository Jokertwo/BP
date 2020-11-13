package my.bak.trafic.view.clipboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.table.where.model.WhereTableModel;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


@Singleton
public class ClipboardImpl implements Clipboard {

    @InjectLogger
    private static Logger logger;

    private List<Column> columns;
    private TreeItem<WhereTableModel> root;
    private BooleanProperty property;
    private boolean isDistinct;


    @Inject
    public ClipboardImpl() {
        property = new SimpleBooleanProperty(true);
        isDistinct = false;
    }


    @Override
    public void store(List<Column> columns, TreeItem<WhereTableModel> root, boolean distinct) {
        logger.info("Store new values to clipboard, {},{}", columns, root);
        this.columns = new ArrayList<>(columns);
        this.root = new TreeItem<>();
        this.root.getChildren().addAll(root.getChildren());
        property.set(false);
        this.isDistinct = distinct;
    }


    @Override
    public BooleanProperty isEmpty() {
        return property;
    }


    @Override
    public TreeItem<WhereTableModel> getRoot() {
        return root;
    }


    @Override
    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public boolean getDistinct() {
        return isDistinct;
    }

}
