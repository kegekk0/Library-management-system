package service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Persistence Manager for managing the EntityManagerFactory lifecycle.
 * <p>
 * This class ensures efficient management of database connections across the application.
 * </p>
 */
public class PersistenceManager {
    private static final String PERSISTENCE_UNIT_NAME = "pjUni";
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Provides a singleton instance of EntityManagerFactory.
     *
     * @return The EntityManagerFactory instance.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            synchronized (PersistenceManager.class) {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
        }
        return entityManagerFactory;
    }

    /**
     * Closes the EntityManagerFactory.
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
