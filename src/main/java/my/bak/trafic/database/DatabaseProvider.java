package my.bak.trafic.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.Alert.AlertType;
import my.bak.trafic.core.event.Bus;
import my.bak.trafic.core.event.types.AlertEvent;
import my.bak.trafic.core.event.types.ConnectionEvent;
import my.bak.trafic.core.event.types.StatusChangeEvent;
import my.bak.trafic.core.plugin.transport.ImportDataBuilder;
import my.bak.trafic.database.entity.*;
import my.bak.trafic.database.service.Service;
import my.bak.trafic.database.service.impl.LocationService;
import my.bak.trafic.database.service.impl.TimeService;
import my.bak.trafic.database.service.impl.TraficDataService;
import my.bak.trafic.database.service.impl.TypeService;
import my.bak.trafic.logger.InjectLogger;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


@Singleton
public class DatabaseProvider implements Database {

    @InjectLogger
    private static Logger logger;

    private Service<Type> typeService;
    private Service<Location> locationService;
    private Service<Time> timeService;
    private Service<TraficData> dataService;

    private MyEntityManagerFactory factory;
    private EntityManager em;
    private Bus bus;


    @Inject
    public DatabaseProvider(MyEntityManagerFactory factory, Bus bus) {
        logger.info("Inicialize database provider");
        this.bus = bus;
        this.factory = factory;
    }


    @Override
    public synchronized void save(List<ImportDataBuilder> data) throws RollbackException {
        logger.debug("Start saving");
        em.getTransaction().begin();
//        em.setFlushMode(FlushModeType.COMMIT);
        List<Time> times = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<TraficData> trafficDatas = new ArrayList<>();
        List<Type> types = new ArrayList<>();

        for (ImportDataBuilder item : data) {
            logger.debug("Saving to db: {}", item);
            Time time = new Time();
            time.setBegin(item.getBeginDate());
            time.setEnd(item.getEndDate());
            if (times.contains(time)) {
                time = times.get(times.indexOf(time));
            } else {
                time = timeService.saveAndGet(time);
                times.add(time);
            }
            Location location = new Location();
            location.setPlace(item.getPlace());
            location.setDirection(item.getDirection());
            if (locations.contains(location)) {
                location = locations.get(locations.indexOf(location));
            } else {
                location = locationService.saveAndGet(location);
                locations.add(location);
            }

            TraficData traficData = new TraficData();
            traficData.setLocation(location);
            traficData.setTime(time);
            if (trafficDatas.contains(traficData)) {
                traficData = trafficDatas.get(trafficDatas.indexOf(traficData));
            } else {
                traficData = dataService.saveAndGet(traficData);
                trafficDatas.add(traficData);
            }
            for (Entry<String, String> entry : item.getData().entrySet()) {

                Type type = new Type();
                type.setType(entry.getKey());
                if (types.contains(type)) {
                    type = types.get(types.indexOf(type));
                } else {
                    type = typeService.saveAndGet(type);
                    types.add(type);
                }

                Value value = new Value();
                value.setData(traficData);
                value.setType(type);
                value.setValue(entry.getValue());

                em.persist(value);
            }
        }
        em.getTransaction().commit();
        logger.debug("Insert done");
    }


    public List<Type> getAllTypeColumns() {
        return typeService.getAll();
    }


    @Override
    public synchronized boolean isConnected() {
        return (em != null && em.isOpen());
    }


    @Override
    public synchronized void connect() {
        logger.info("Open connection to db");
        try {
            em = factory.getFatory().createEntityManager();
            typeService = new TypeService(em);
            locationService = new LocationService(em);
            timeService = new TimeService(em);
            dataService = new TraficDataService(em);
            bus.publishEvent(new ConnectionEvent(true));
            bus.publishEvent(new StatusChangeEvent("Connected", "You are succesfully connected to database"));
            logger.info("Successfully connect to db");
        } catch (Exception e) {
            bus.publishEvent(new StatusChangeEvent("Database error", e.getMessage()));
            bus.publishEvent(new ConnectionEvent(false));
            bus.publishEvent(new AlertEvent(AlertType.ERROR, "Database error", "Cannot connect to database", e));
            logger.fatal("Unable to connect to database", e);
        }
    }


    @Override
    public synchronized void disconect() {
        if (em != null && em.isOpen()) {
            logger.info("Close connection to db");
            em.close();
            bus.publishEvent(new ConnectionEvent(false));
            bus.publishEvent(new StatusChangeEvent("Disconnected", "You are disconnected"));
        } else {
            logger.error("Cannot disconect from database becaose app was not connected");
        }
    }


    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }


    @Override
    public synchronized <Data> List<Data> select(TypedQuery<Data> query) {
        return query.getResultList();
    }


    @Override
    public List<Location> getAllLocation() {
        return locationService.getAll();
    }


    @Override
    public <Data> TypedQuery<Data> createTypedQuery(CriteriaQuery<Data> query) {
        return em.createQuery(query);
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
