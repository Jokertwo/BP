package my.bak.trafic.database.query.visitor.impl;

import my.bak.trafic.database.query.visitor.WhereVisitor;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;


@SuppressWarnings({"rawtypes", "unchecked"})
public class QueryOperatorVisitorTest {
    private WhereVisitor<Predicate> visitor;
    private CriteriaBuilder cb;
    private Path path;
    private Object object;


    @Before
    public void setUp() throws Exception {
        object = new String("Ahoj");
        cb = EasyMock.createNiceMock(CriteriaBuilder.class);
        path = EasyMock.createNiceMock(Path.class);
        visitor = new QueryOperatorVisitor(cb, path, object);
    }


    @Test
    public void testEquals() {
        EasyMock.expect(cb.equal(path, object)).andReturn(null).once();
        EasyMock.replay(cb, path);
        visitor.equals();
        EasyMock.verify(cb, path);
    }


    @Test
    public void testGreaterThan() {
        EasyMock.expect(cb.greaterThan(path, (Comparable) object)).andReturn(null).once();
        EasyMock.replay(cb, path);
        visitor.greaterThan();
        EasyMock.verify(cb, path);
    }


    @Test
    public void testLessThen() {
        EasyMock.expect(cb.lessThan(path, (Comparable) object)).andReturn(null).once();
        EasyMock.replay(cb, path);
        visitor.lessThen();
        EasyMock.verify(cb, path);
    }


    @Test
    public void testGreaterThanOrEquals() {
        EasyMock.expect(cb.greaterThanOrEqualTo(path, (Comparable) object)).andReturn(null).once();
        EasyMock.replay(cb, path);
        visitor.greaterThanOrEquals();
        EasyMock.verify(cb, path);
    }


    @Test
    public void testLessThanOrEquals() {
        EasyMock.expect(cb.lessThanOrEqualTo(path, (Comparable) object)).andReturn(null).once();
        EasyMock.replay(cb, path);
        visitor.lessThanOrEquals();
        EasyMock.verify(cb, path);
    }


    @Test
    public void testNotEquals() {
        EasyMock.expect(cb.notEqual(path, (Comparable) object)).andReturn(null).once();
        EasyMock.replay(cb, path);
        visitor.notEquals();
        EasyMock.verify(cb, path);
    }

}
