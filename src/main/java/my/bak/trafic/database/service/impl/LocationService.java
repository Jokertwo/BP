package my.bak.trafic.database.service.impl;

import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.service.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public class LocationService implements Service<Location> {

    private EntityManager entityManager;


    public LocationService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    @Override
    public void save(List<Location> entity) {
        entity.forEach(item -> entityManager.persist(item));
    }


    @Override
    public Optional<Location> get(Location values) {
        @SuppressWarnings("unchecked")
        List<Location> location = entityManager.createNamedQuery("findIfLocationExist")
                .setParameter("place", values.getPlace()).setParameter("direction", values.getDirection()).getResultList();
        return location.isEmpty() ? Optional.empty() : Optional.of(location.get(0));
    }


    @Override
    public List<Location> getAll() {
        return getAll(Location.class);
    }


    @Override
    public EntityManager getManager() {
        return entityManager;
    }

}
