package my.bak.trafic.database.service.impl;

import my.bak.trafic.database.entity.TraficData;
import my.bak.trafic.database.service.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;


public class TraficDataService implements Service<TraficData> {

    private EntityManager entityManager;


    public TraficDataService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    @Override
    public List<TraficData> getAll() {
        return getAll(TraficData.class);
    }


    @Override
    public void save(List<TraficData> entity) {
        entity.forEach(item -> entityManager.persist(item));

    }


    @Override
    public Optional<TraficData> get(TraficData value) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TraficData> criteria = criteriaBuilder.createQuery(TraficData.class);
        Root<TraficData> root = criteria.from(TraficData.class);

        criteria.where(criteriaBuilder.equal(root.get("time"), value.getTime()),
                criteriaBuilder.equal(root.get("location"), value.getLocation()));

        List<TraficData> data = entityManager.createQuery(criteria).getResultList();
        return data.isEmpty() ? Optional.empty() : Optional.of(data.get(0));
    }


    @Override
    public EntityManager getManager() {
        return entityManager;
    }

}
