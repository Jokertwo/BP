package my.bak.trafic.database.service.impl;

import my.bak.trafic.database.entity.Time;
import my.bak.trafic.database.service.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public class TimeService implements Service<Time> {

    private EntityManager entityManager;


    public TimeService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    @Override
    public List<Time> getAll() {
        return getAll(Time.class);
    }


    @Override
    public void save(List<Time> entity) {
        entity.forEach(item -> entityManager.persist(item));

    }


    @Override
    public Optional<Time> get(Time values) {
        @SuppressWarnings("unchecked")
        List<Time> times = entityManager.createNamedQuery("findIfTimeExist").setParameter("begin", values.getBegin())
                .setParameter("end", values.getEnd()).getResultList();
        return times.isEmpty() ? Optional.empty() : Optional.of(times.get(0));
    }


    @Override
    public EntityManager getManager() {
        return entityManager;
    }

}
