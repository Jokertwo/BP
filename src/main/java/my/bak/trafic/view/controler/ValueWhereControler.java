package my.bak.trafic.view.controler;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.visitor.impl.ValueEditorVisitor;
import my.bak.trafic.view.NodeWrapper;
import my.bak.trafic.view.ValueWhere;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ValueWhereControler implements ValueWhere {

    private static Logger logger = LogManager.getLogger();
    private HBox container;
    private NodeWrapper active;


    public ValueWhereControler(HBox container) {
        super();
        this.container = container;
        active = new NodeWrapper(new TextField());
        container.getChildren().add(active.getNode());
    }


    @Override
    public Object getValue() {
        return active.getValue();
    }


    @Override
    public void changeEditor(Column column) {
        container.getChildren().clear();
        NodeWrapper old = active;
        active = null;
        if (column != null) {
            active = column.visit(new ValueEditorVisitor());
            container.getChildren().add(active.getNode());
        }
        logger.info("Change editor from: {}, to: {}", old, active);
    }

}
