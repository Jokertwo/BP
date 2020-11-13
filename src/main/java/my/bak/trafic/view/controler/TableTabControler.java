package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.database.query.Query;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.visitor.impl.ColumnBuilderVisitor;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.clipboard.Clipboard;
import my.bak.trafic.view.table.view.model.ViewTableModel;
import my.bak.trafic.view.util.ViewUtil;
import org.apache.logging.log4j.Logger;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class TableTabControler extends AbstractContentControler {

    @InjectLogger
    private static Logger logger;

    @FXML
    private TableView<ViewTableModel> tableView;

    private ThreadPool pool;
    private Query query;
    private ViewUtil viewUtil;


    @Inject
    public TableTabControler(ThreadPool pool,
                             Query query,
                             ViewUtil viewUtil,
                             Clipboard clipboard) {
        super(logger, clipboard);
        this.pool = pool;
        this.query = query;
        this.viewUtil = viewUtil;
    }


    @Override
    public void handleRunBtn(ActionEvent event) {
        // remove old columns
        tableView.getColumns().clear();
        // remove old models
        tableView.getItems().clear();

        // create new columns
        ColumnBuilderVisitor visitor = new ColumnBuilderVisitor();
        for (Column item : getQueryControler().getColumns()) {
            tableView.getColumns().add(item.visit(visitor));
        }

        pool.execute(() -> {
            // create copy
            List<Column> copyColums = getQueryControler().getColumns().stream().collect(Collectors.toList());
            CriteriaQuery<Tuple> select = query.createSelectQuery(copyColums, getQueryControler().getRoot(),
                    getQueryControler().isDistinct());

            // get data from database
            List<Tuple> list = query.executeSelectQuery(select);

            // check if list is not empty
            if (!list.isEmpty()) {
                final List<ViewTableModel> models = new ArrayList<>();
                // create rows
                list.stream().map(row -> viewUtil.createViewTableModel(copyColums, row))
                        .forEach(models::add);
                // add rows to table
                pool.getFXExecutor().execute(() -> tableView.getItems().addAll(models));
            }
        });
    }


    /**
     * Initialize tab
     *
     * @param location  not used
     * @param resources not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        getRunBtn().setText("Execute");
        getRunBtn().setDisable(true);
    }


    @Override
    public boolean close() {
        return true;
    }


    @Override
    public String title() {
        return "View";
    }

}
