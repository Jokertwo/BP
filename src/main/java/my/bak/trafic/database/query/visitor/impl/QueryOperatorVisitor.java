package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.database.query.visitor.WhereVisitor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;


@SuppressWarnings({"rawtypes", "unchecked"})
public class QueryOperatorVisitor implements WhereVisitor<Predicate> {

    private CriteriaBuilder cb;
    private Path expresion;
    private Object value;


    public QueryOperatorVisitor(CriteriaBuilder cb, Selection expresion, Object value) {
        super();
        this.cb = cb;
        this.expresion = (Path) expresion;
        this.value = value;
    }


    @Override
    public Predicate equals() {
        return cb.equal(expresion, value);
    }


    @Override
    public Predicate greaterThan() {
        return cb.greaterThan(expresion, (Comparable) value);
    }


    @Override
    public Predicate lessThen() {
        return cb.lessThan(expresion, (Comparable) value);
    }


    @Override
    public Predicate greaterThanOrEquals() {
        return cb.greaterThanOrEqualTo(expresion, (Comparable) value);
    }


    @Override
    public Predicate lessThanOrEquals() {
        return cb.lessThanOrEqualTo(expresion, (Comparable) value);
    }


    @Override
    public Predicate notEquals() {
        return cb.notEqual(expresion, value);
    }

}
