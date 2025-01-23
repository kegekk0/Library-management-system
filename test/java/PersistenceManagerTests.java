import org.junit.jupiter.api.*;
import service.PersistenceManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersistenceManagerTests {

    @BeforeEach
    void clearEntityManagerFactory() {
        PersistenceManager.closeEntityManagerFactory();
    }

    @Test
    void testEntityManagerFactoryIsSingleton() {
        EntityManagerFactory emf1 = PersistenceManager.getEntityManagerFactory();
        assertNotNull(emf1, "EntityManagerFactory should not be null.");

        EntityManagerFactory emf2 = PersistenceManager.getEntityManagerFactory();
        assertNotNull(emf2, "EntityManagerFactory should not be null.");

        assertSame(emf1, emf2, "EntityManagerFactory should be a singleton.");
    }

    @Test
    void testEntityManagerFactoryClosesProperly() {
        EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();
        assertNotNull(emf, "EntityManagerFactory should not be null.");
        assertTrue(emf.isOpen(), "EntityManagerFactory should be open initially.");

        PersistenceManager.closeEntityManagerFactory();
        assertFalse(emf.isOpen(), "EntityManagerFactory should be closed after calling closeEntityManagerFactory.");
    }

    @Test
    void testEntityManagerFactoryRecreationAfterClose() {
        EntityManagerFactory emf1 = PersistenceManager.getEntityManagerFactory();
        PersistenceManager.closeEntityManagerFactory();
        assertFalse(emf1.isOpen(), "EntityManagerFactory should be closed.");

        EntityManagerFactory emf2 = PersistenceManager.getEntityManagerFactory();
        assertNotNull(emf2, "A new EntityManagerFactory should be created after closing the old one.");
        assertTrue(emf2.isOpen(), "The new EntityManagerFactory should be open.");

        assertNotSame(emf1, emf2, "A new EntityManagerFactory instance should be created after closing the old one.");
    }

    @Test
    void testPersistenceUnitInitialization() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("pjUni");
            assertNotNull(emf, "EntityManagerFactory should not be null.");
            assertTrue(emf.isOpen(), "EntityManagerFactory should be open.");
            emf.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to initialize EntityManagerFactory: " + e.getMessage());
        }
    }
}
