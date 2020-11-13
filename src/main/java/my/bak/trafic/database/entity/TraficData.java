package my.bak.trafic.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
public class TraficData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_id")
    private Time time;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "data")
    private List<Value> values;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TraficData) {
            TraficData temp = (TraficData) obj;
            return temp.getLocation().equals(location) && temp.getTime().equals(time);
        }
        return super.equals(obj);
    }
}
