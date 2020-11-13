package my.bak.trafic.database;

import my.bak.trafic.core.plugin.transport.ImportDataBuilder;
import my.bak.trafic.database.entity.Location;
import my.bak.trafic.database.entity.Type;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public interface Database {
    /**
     * Return entity manager
     *
     * @return
     */
    EntityManager getEntityManager();


    CriteriaBuilder getCriteriaBuilder();


    /**
     * Save data to database
     *
     * @param data pojo with data which to be save
     */
    void save(List<ImportDataBuilder> data) throws RollbackException;


    /**
     * Select data from database
     *
     * @param query
     * @return
     */
    <Data> List<Data> select(TypedQuery<Data> query);


    /**
     * Create from criteria query TypedQuery
     *
     * @param query
     * @return
     */
    <Data> TypedQuery<Data> createTypedQuery(CriteriaQuery<Data> query);


    /**
     * Connect to db
     */
    void connect();


    /**
     * Disconect from db
     *
     * @return
     */
    void disconect();


    /**
     * Return all type columns
     *
     * @return
     */
    List<Type> getAllTypeColumns();


    /**
     * Return true if is establish connection to database
     *
     * @return
     */
    boolean isConnected();


    /**
     * Return all locations
     *
     * @return
     */
    List<Location> getAllLocation();
}
