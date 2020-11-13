package my.bak.trafic.view.util;

import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.database.query.visitor.impl.ModelBuilderVisitor;
import my.bak.trafic.view.table.view.model.ViewTableModel;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;


public class ViewUtilImpl implements ViewUtil {

    @Override
    public ViewTableModel createViewTableModel(List<Column> columns, Tuple row) {
        ViewTableModel model = new ViewTableModel();
        Visitor<ViewTableModel> modelVisitor = new ModelBuilderVisitor(row, model);
        for (Column item : columns) {
            model = item.visit(modelVisitor);
        }
        return model;
    }


    @Override
    public Optional<TitledPane> createStaceTrace(Throwable e) {
        TitledPane titlePane = null;
        if (e != null) {
            titlePane = new TitledPane();
            titlePane.setExpanded(false);
            titlePane.setText("Click for more info");
            titlePane.setAnimated(true);
            TextArea area = new TextArea();
            area.setText(ExceptionUtils.getFullStackTrace(e));
            area.setEditable(false);
            titlePane.setContent(area);
        }
        return Optional.ofNullable(titlePane);
    }

}
