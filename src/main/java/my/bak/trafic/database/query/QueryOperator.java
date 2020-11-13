package my.bak.trafic.database.query;

import my.bak.trafic.database.query.visitor.WhereVisitor;


public enum QueryOperator {
    EQUAL {
        public <Type> Type accept(WhereVisitor<Type> visitor) {
            return visitor.equals();
        }
    },
    GREATER_THAN {
        public <Type> Type accept(WhereVisitor<Type> visitor) {
            return visitor.greaterThan();
        }
    },
    LESS_THEN {
        public <Type> Type accept(WhereVisitor<Type> visitor) {
            return visitor.lessThen();
        }
    },
    GREATER_THAN_OR_EQUAL {
        public <Type> Type accept(WhereVisitor<Type> visitor) {
            return visitor.greaterThanOrEquals();
        }
    },
    LESS_THAN_OR_EQUAL {
        public <Type> Type accept(WhereVisitor<Type> visitor) {
            return visitor.lessThanOrEquals();
        }
    },
    NOT_EQUAL {
        public <Type> Type accept(WhereVisitor<Type> visitor) {
            return visitor.notEquals();
        }
    };

    public abstract <E> E accept(WhereVisitor<E> visitor);

}
