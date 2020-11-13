package my.bak.trafic.database;

import javax.persistence.EntityManagerFactory;

public interface MyEntityManagerFactory {


    /**
     * Create entity manager factory with configuration based on user setting
     *
     * @return {@link EntityManagerFactory}
     */
    EntityManagerFactory getFatory();
}
