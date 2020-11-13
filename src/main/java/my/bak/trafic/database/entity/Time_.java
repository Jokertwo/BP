package my.bak.trafic.database.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Time.class)
public class Time_ {
    public static volatile SingularAttribute<Time, Long> id;
    public static volatile SingularAttribute<Time, Date> begin;
    public static volatile SingularAttribute<Time, Date> end;
}
