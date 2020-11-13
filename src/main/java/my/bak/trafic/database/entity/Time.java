package my.bak.trafic.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@NamedQuery(name = "findIfTimeExist", query = "SELECT c FROM Time c WHERE c.begin = :begin AND c.end = :end")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date begin;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "time")
    private List<TraficData> data;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Time) {
            Time time = (Time) obj;
            return time.getBegin().equals(begin) && time.getEnd().equals(end);
        }
        return super.equals(obj);
    }
}
