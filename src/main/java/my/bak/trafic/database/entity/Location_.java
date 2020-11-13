package my.bak.trafic.database.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Location.class)
public class Location_ {
    public static volatile SingularAttribute<Location, Long> id;
    public static volatile SingularAttribute<Location, String> place;
    public static volatile SingularAttribute<Location, String> direction;
}
