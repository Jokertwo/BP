package my.bak.trafic.database.service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;


public interface Service<Entity> {

    List<Entity> getAll();


    default List<Entity> getAll(Class<Entity> clazz) {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Entity> query = cb.createQuery(clazz);

        Root<Entity> root = query.from(clazz);
        query = query.select(root).distinct(true);
        TypedQuery<Entity> typedQuery = getManager().createQuery(query);
        return typedQuery.getResultList();
    }


    void save(List<Entity> entity);


    Optional<Entity> get(Entity entity);


    EntityManager getManager();


    default Entity saveAndGet(Entity entity) {
        Optional<Entity> value = get(entity);
        if (value.isPresent()) {
            return value.get();
        }
        getManager().persist(entity);
        getManager().flush();
        return entity;

    }

}
