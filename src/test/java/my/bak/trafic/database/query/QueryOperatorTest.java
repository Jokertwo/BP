package my.bak.trafic.database.query;

import my.bak.trafic.database.query.visitor.WhereVisitor;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.Predicate;


public class QueryOperatorTest {
    private WhereVisitor<Predicate> visitor;


    @Before
    public void setUp() throws Exception {
        visitor = EasyMock.createNiceMock(WhereVisitor.class);
    }


    @Test
    public void testEqualsAccept() {
        EasyMock.expect(visitor.equals()).andReturn(null).once();
        runTest(QueryOperator.EQUAL);
    }


    @Test
    public void testLessAccept() {
        EasyMock.expect(visitor.lessThen()).andReturn(null).once();
        runTest(QueryOperator.LESS_THEN);
    }


    @Test
    public void testGreaterAccept() {
        EasyMock.expect(visitor.greaterThan()).andReturn(null).once();
        runTest(QueryOperator.GREATER_THAN);
    }


    @Test
    public void testLessThanOrEqualsAccept() {
        EasyMock.expect(visitor.lessThanOrEquals()).andReturn(null).once();
        runTest(QueryOperator.LESS_THAN_OR_EQUAL);
    }


    @Test
    public void testGreaterTheOrEqualsAccept() {
        EasyMock.expect(visitor.greaterThanOrEquals()).andReturn(null).once();
        runTest(QueryOperator.GREATER_THAN_OR_EQUAL);
    }


    @Test
    public void testNotEquals() {
        EasyMock.expect(visitor.notEquals()).andReturn(null).once();
        runTest(QueryOperator.NOT_EQUAL);
    }


    private void runTest(QueryOperator operator) {
        EasyMock.replay(visitor);
        operator.accept(visitor);
        EasyMock.verify(visitor);
    }

}
