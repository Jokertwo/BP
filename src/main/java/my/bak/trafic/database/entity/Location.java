package my.bak.trafic.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NamedQuery(name = "findIfLocationExist", query = "SELECT c FROM Location c WHERE c.place = :place AND c.direction = :direction")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String place;
    @Column
    private String direction;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    private List<TraficData> data;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location temp = (Location) obj;
            return temp.getDirection().equals(direction) && temp.getPlace().equals(place);
        }
        return super.equals(obj);
    }
}
