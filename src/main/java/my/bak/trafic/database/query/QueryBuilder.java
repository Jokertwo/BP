package my.bak.trafic.database.query;

import com.google.inject.Inject;
import javafx.scene.control.TreeItem;
import my.bak.trafic.database.Database;
import my.bak.trafic.database.query.column.*;
import my.bak.trafic.database.query.visitor.Visitor;
import my.bak.trafic.database.query.visitor.impl.CriteriaVisitor;
import my.bak.trafic.database.query.visitor.impl.IdVisitor;
import my.bak.trafic.database.query.visitor.impl.QueryOperatorVisitor;
import my.bak.trafic.logger.InjectLogger;
import my.bak.trafic.view.table.where.model.WhereTableModel;
import org.apache.logging.log4j.Logger;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class QueryBuilder implements Query {

    private Database database;
    @InjectLogger
    private static Logger logger;


    @Inject
    public QueryBuilder(Database database) {
        super();
        this.database = database;
    }


    @Override
    public List<Column> getAllColumns() {
        List<Column> columns = new ArrayList<>();
        columns.add(new BeginColumn());
        columns.add(new EndColumn());
        columns.add(new DirectionColumn());
        columns.add(new PlaceColumn());
        columns.add(new TypeColumn());
        columns.add(new ValueColumn());
        return columns;
    }


    @Override
    public CriteriaQuery<Tuple> createSelectQuery(List<Column> columns,
                                                  TreeItem<WhereTableModel> root,
                                                  boolean distinct) {
        CriteriaBuilder cb = database.getCriteriaBuilder();

        CriteriaQuery<Tuple> q = cb.createQuery(Tuple.class);

        TableJoiner joiner = new TableJoinerImpl(columns, root, q);

        Visitor<Selection<?>> critVisitor = new CriteriaVisitor(joiner);
        Visitor<Optional<Selection<Long>>> idVisitor = new IdVisitor(joiner);
        List<Selection<?>> paths = new ArrayList<>();
        for (Column item : columns) {
            // get entity id
            item.visit(idVisitor).ifPresent(paths::add);
            paths.add(item.visit(critVisitor));
        }

        return q.select(cb.tuple(paths.toArray(new Selection[paths.size()])))
                .where(createWhere(root, critVisitor, cb)).distinct(distinct);
    }


    @Override
    public List<Tuple> executeSelectQuery(CriteriaQuery<Tuple> query) {
        return database.select(database.createTypedQuery(query));
    }


    private Predicate createWhere(TreeItem<WhereTableModel> root,
                                  Visitor<Selection<?>> critVisitor,
                                  CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        for (TreeItem<WhereTableModel> item : root.getChildren()) {
            if (item.getValue().isContainer()) {
                createWhere(item, critVisitor, cb);
            } else {
                predicates.add(item.getValue().getOperator().accept(new QueryOperatorVisitor(cb,
                        item.getValue().getColumn().visit(critVisitor), item.getValue().getValue())));
            }
        }
        return root.getValue().getCombination().equals(QueryCombination.AND)
                ? cb.and(predicates.toArray(new Predicate[predicates.size()]))
                : cb.or(predicates.toArray(new Predicate[predicates.size()]));
    }


    @Override
    public long getCount(CriteriaQuery<Tuple> query) {
        CriteriaQuery<Long> countQuery = database.getCriteriaBuilder().createQuery(Long.class);

        Root<Tuple> countRoot = countQuery.from(query.getResultType());

        TypedQuery<Long> queryCount = database
                .createTypedQuery(countQuery.select(database.getCriteriaBuilder().count(countRoot)));
        return queryCount.getSingleResult();
    }

}
