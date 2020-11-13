package my.bak.trafic.database.query;

import javafx.scene.control.TreeItem;
import my.bak.trafic.database.entity.*;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.visitor.impl.RootVisitor;
import my.bak.trafic.view.table.where.model.WhereTableModel;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class TableJoinerImpl implements TableJoiner {

    private Path<Time> timePath;
    private Path<Location> locationPath;
    private Path<Value> valuePath;
    private Path<Type> typePath;


    public TableJoinerImpl(List<Column> columns, TreeItem<WhereTableModel> Path, CriteriaQuery<Tuple> q) {
        List<Column> columnList = convertToList(columns, Path);
        if (columnList.stream().distinct().limit(2).count() <= 1) {
            RootVisitor rootVisitor = new RootVisitor(q);
            columns.get(0).visit(rootVisitor);
            timePath = rootVisitor.getTimePath();
            locationPath = rootVisitor.getLocationPath();
            valuePath = rootVisitor.getValuePath();
            typePath = rootVisitor.getTypePath();
        } else {
            Root<Value> qr = q.from(Value.class);
            Join<Value, TraficData> data = qr.join("data");
            timePath = data.join("time");
            locationPath = data.join("location");
            typePath = qr.join("type");
            valuePath = qr;
        }
    }


    private List<Column> convertToList(List<Column> columns, TreeItem<WhereTableModel> root) {
        List<Column> clazzList = new ArrayList<>(columns);
        clazzList.addAll(convertTree(root));
        return clazzList;
    }


    private List<Column> convertTree(TreeItem<WhereTableModel> root) {
        List<Column> isSame = new ArrayList<>();
        for (TreeItem<WhereTableModel> item : root.getChildren()) {
            if (item.getValue().isContainer()) {
                isSame.addAll(convertTree(item));
            } else {
                isSame.add(item.getValue().getColumn());
            }
        }
        return isSame;
    }


    @Override
    public Path<Time> getTimePath() {
        return timePath;
    }


    @Override
    public Path<Location> getLocationPath() {
        return locationPath;
    }


    @Override
    public Path<Value> getValuePath() {
        return valuePath;
    }


    @Override
    public Path<Type> getTypePath() {
        return typePath;
    }

}
