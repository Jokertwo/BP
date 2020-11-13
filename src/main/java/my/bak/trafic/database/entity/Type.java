package my.bak.trafic.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NamedQuery(name = "findIfTypeExist", query = "SELECT c FROM Type c WHERE c.type = :type")

public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String type;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    private List<Value> values;


    @Override
    public String toString() {
        return type;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Type) {
            Type temp = (Type) obj;
            return type.equals(temp.getType());
        }
        return super.equals(obj);
    }
}
