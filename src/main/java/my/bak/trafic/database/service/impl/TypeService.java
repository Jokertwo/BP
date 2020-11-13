package my.bak.trafic.database.service.impl;

import my.bak.trafic.database.entity.Type;
import my.bak.trafic.database.service.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public class TypeService implements Service<Type> {

    private EntityManager entityManager;


    public TypeService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    @Override
    public List<Type> getAll() {
        return getAll(Type.class);
    }


    @Override
    public void save(List<Type> entity) {
        entity.forEach(item -> entityManager.persist(item));
    }


    @Override
    public Optional<Type> get(Type values) {
        @SuppressWarnings("unchecked")
        List<Type> types = entityManager.createNamedQuery("findIfTypeExist").setParameter("type", values.getType())
                .getResultList();
        return types.isEmpty() ? Optional.empty() : Optional.of(types.get(0));
    }


    @Override
    public EntityManager getManager() {
        return entityManager;
    }

}
