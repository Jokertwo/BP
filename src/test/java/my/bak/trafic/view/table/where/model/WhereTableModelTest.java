package my.bak.trafic.view.table.where.model;

import my.bak.trafic.database.query.QueryCombination;
import my.bak.trafic.database.query.QueryOperator;
import my.bak.trafic.database.query.column.Column;
import my.bak.trafic.database.query.column.EndColumn;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class WhereTableModelTest {

    private WhereTableModel model;


    @Before
    public void setUp() throws Exception {
        model = new WhereTableModel();
    }


    @Test
    public void testGetColumn() {
        Column column = new EndColumn();
        model.setColumn(column);
        assertEquals(column, model.getColumn());
        assertEquals(column, model.getColumnProperty().getValue());
    }


    @Test
    public void testGetOperator() {
        model.setOperator(QueryOperator.EQUAL);
        assertEquals(QueryOperator.EQUAL, model.getOperator());
        assertEquals(QueryOperator.EQUAL, model.getOperatorProperty().getValue());
    }


    @Test
    public void testGetValue() {
        String value = "value";
        model.setValue(value);
        assertEquals(value, model.getValue());
        assertEquals(value, model.getValueProperty().getValue());
    }


    @Test
    public void testGetCombination() {
        model.setCombination(QueryCombination.AND);
        assertEquals(QueryCombination.AND, model.getCombination());
        assertEquals(QueryCombination.AND, model.getCombinationProperty().getValue());
    }


    @Test
    public void testIsNotSetContainer() {
        assertEquals(false, model.isContainer());
    }


    @Test
    public void testIsContainer() {
        model.setCombination(QueryCombination.AND);
        assertEquals(true, model.isContainer());
    }

}
