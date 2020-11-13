package my.bak.trafic.view.controler;

import com.google.inject.Inject;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import my.bak.trafic.core.ThreadPool;
import my.bak.trafic.database.query.Query;
import my.bak.trafic.database.query.QueryCombination;
import my.bak.trafic.database.query.QueryOperator;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.QueryController;
import my.bak.trafic.view.ValueWhere;
import my.bak.trafic.view.table.where.model.WhereTableModel;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class QueryControlerImpl implements Initializable, QueryController {

    @InjectLogger
    private static Logger logger;

    @FXML
    private SplitPane content;

    @FXML
    private ComboBox<String> tableComboBox;

    @FXML
    private Button addTableBtn;

    @FXML
    private Button addWhereBtn;

    @FXML
    private ListView<String> tableListView;

    @FXML
    private ComboBox<Column> columnComboBox;

    @FXML
    private Button addColumnBtn;

    @FXML
    private ListView<Column> columnListView;

    @FXML
    private TreeTableColumn<WhereTableModel, Column> columnField;

    @FXML
    private TreeTableColumn<WhereTableModel, QueryOperator> columnOperator;

    @FXML
    private TreeTableColumn<WhereTableModel, Object> columnValue;

    @FXML
    private TreeTableColumn<WhereTableModel, QueryCombination> columnComb;

    @FXML
    private TreeTableView<WhereTableModel> whereTable;

    @FXML
    private Button removeBtn;

    @FXML
    private ComboBox<Column> fieldCB;

    @FXML
    private ComboBox<QueryOperator> operatorCB;

    @FXML
    private HBox valueBox;

    @FXML
    private Button removeWhereBtn;

    @FXML
    private CheckBox distinctCeB;

    private TreeItem<WhereTableModel> root;
    private ContextMenu whereContextMenu;
    private ContextMenu columnContextMenu;
    private Query query;
    private ValueWhere valueWhere;
    private ThreadPool pool;


    @Inject
    public QueryControlerImpl(Query query, ThreadPool pool) {
        this.query = query;
        this.pool = pool;
    }


    @FXML
    public void handleRemoveBtn(ActionEvent event) {
        if (!columnListView.getSelectionModel().isEmpty()) {
            ObservableList<Column> columns = columnListView.getSelectionModel().getSelectedItems();
            String[] array = columns.stream().map(Column::getColumnName).collect(Collectors.toList())
                    .toArray(new String[columns.size()]);
            logger.info("Removing column from list view: {}", Arrays.toString(array));
            columnListView.getItems().removeAll(columns);
        }
    }


    @FXML
    void handleRemoveWhereBtn(ActionEvent event) {
        deleteWhereItem(whereTable.getSelectionModel().getSelectedItem());
    }


    private void deleteWhereItem(TreeItem<WhereTableModel> selection) {
        if (selection != null && !selection.equals(root)) {
            logger.info("Removing '{}' from where table");
            root.getChildren().remove(selection);
        }
    }


    /**
     * Handler for add column button
     *
     * @param event btn event
     */
    @FXML
    public void handleAddColumn(ActionEvent event) {
        if (columnComboBox.getSelectionModel().getSelectedItem() != null) {
            Column selection = columnComboBox.getSelectionModel().getSelectedItem();
            if (columnComboBox.getItems().isEmpty()) {
                logger.warn("Can not add empty selection");
            } else if (columnListView.getItems().contains(selection)) {
                logger.warn("Is not posible add two same columns");
            } else {
                logger.info("Add {}", selection);
                columnListView.getItems().add(selection);
            }
        }
    }


    @FXML
    public void handleAddWhere(ActionEvent event) {
        TreeItem<WhereTableModel> selection = whereTable.getSelectionModel().getSelectedItem();
        if (selection == null) {
            selection = root;
        }
        WhereTableModel model = new WhereTableModel();
        model.setColumn(fieldCB.getValue());
        model.setOperator(operatorCB.getValue());
        model.setValue(valueWhere.getValue());
        TreeItem<WhereTableModel> item = new TreeItem<>(model);
        if (selection.equals(root) || selection.getValue().isContainer()) {
            selection.getChildren().add(item);
            selection.setExpanded(true);
        } else {
            selection.getParent().getChildren().add(item);
            selection.getParent().setExpanded(true);
        }
        whereTable.requestFocus();
        whereTable.getSelectionModel().selectLast();

    }


    /**
     * Create context menu for column list view
     */
    private void createColumnContextMenu() {
        columnContextMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove");
        remove.disableProperty().bind(columnListView.getSelectionModel().selectedItemProperty().isNull());
        remove.setOnAction(this::handleRemoveBtn);
        columnContextMenu.getItems().add(remove);
        // hide when menu lost focus
        columnContextMenu.setAutoHide(true);
    }


    /**
     * Create context menu for where table
     */
    private void createWhereContextMenu() {
        whereContextMenu = new ContextMenu();
        Menu container = new Menu("Add");

        MenuItem and = new MenuItem("And");
        and.setOnAction(this::addAnd);
        container.getItems().add(and);

        MenuItem or = new MenuItem("Or");
        or.setOnAction(this::addOr);
        container.getItems().add(or);

        MenuItem remove = new MenuItem("Remove");
        remove.disableProperty()
                .bind(whereTable.getSelectionModel().selectedItemProperty().isNull()
                        .or(whereTable.getSelectionModel().selectedItemProperty().isEqualTo(root)));
        remove.setOnAction(this::handleRemoveBtn);

        whereContextMenu.getItems().add(container);
        whereContextMenu.getItems().add(remove);
        // hide when menu lost focus
        whereContextMenu.setAutoHide(true);
    }


    private void addAnd(ActionEvent event) {
        addContainer(QueryCombination.AND);
    }


    private void addOr(ActionEvent event) {
        addContainer(QueryCombination.OR);
    }


    private void addContainer(QueryCombination combination) {
        TreeItem<WhereTableModel> selection = whereTable.getSelectionModel().getSelectedItem();
        if (selection == null) {
            selection = root;
        }

        WhereTableModel model = new WhereTableModel();
        model.setCombination(combination);
        TreeItem<WhereTableModel> item = new TreeItem<>(model);

        if (selection.equals(root) || selection.getValue().isContainer()) {
            selection.getChildren().add(item);
            selection.setExpanded(true);
        } else {
            selection.getParent().getChildren().add(item);
            selection.getParent().setExpanded(true);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // inicialize table
        columnField.setCellValueFactory(cellData -> cellData.getValue().getValue().getColumnProperty());
        columnOperator.setCellValueFactory(cellData -> cellData.getValue().getValue().getOperatorProperty());
        columnValue.setCellValueFactory(cellData -> cellData.getValue().getValue().getValueProperty());
        columnComb.setCellValueFactory(cellData -> cellData.getValue().getValue().getCombinationProperty());

        // set root
        WhereTableModel rootModel = new WhereTableModel();
        rootModel.setCombination(QueryCombination.AND);
        root = new TreeItem<>(rootModel);
        root.setExpanded(true);
        whereTable.setRoot(root);

        // add context menu to where table
        createWhereContextMenu();
        whereTable.setContextMenu(whereContextMenu);

        // add context menu to column list view
        createColumnContextMenu();
        columnListView.setContextMenu(columnContextMenu);

        // enable multiselection
        columnListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // disable removeColumnBtn while selection is empty
        removeBtn.disableProperty().bind(columnListView.getSelectionModel().selectedItemProperty().isNull());

        valueWhere = new ValueWhereControler(valueBox);

        // fill combo box
        columnComboBox.getItems().addAll(query.getAllColumns());
        fieldCB.getItems().addAll(query.getAllColumns());
        fieldCB.valueProperty().addListener((ob, oldValue, newValue) -> valueWhere.changeEditor(newValue));
        operatorCB.getItems().addAll(Arrays.asList(QueryOperator.values()));

        //disable add where
        addWhereBtn.disableProperty().bind(fieldCB.getSelectionModel().selectedItemProperty().isNull()
                .or(operatorCB.getSelectionModel().selectedItemProperty().isNull()));

        // disable policy for removeWhere btn
        removeWhereBtn.disableProperty()
                .bind(whereTable.getSelectionModel().selectedItemProperty().isNull()
                        .or(whereTable.getSelectionModel().selectedItemProperty().isEqualTo(root)));
    }


    @Override
    public boolean isDistinct() {
        return distinctCeB.isSelected();
    }

    @Override
    public void setDistinct(boolean distinct) {
        pool.getFXExecutor().execute(() -> distinctCeB.setSelected(distinct));
    }


    @Override
    public ObservableList<Column> getColumns() {
        return columnListView.getItems();
    }


    @Override
    public TreeItem<WhereTableModel> getRoot() {
        return root;
    }


    @Override
    public Node getContent() {
        return content;
    }

}
