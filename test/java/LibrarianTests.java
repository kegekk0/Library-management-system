import entity.Librarian;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarianTests {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
    }

    @Test
    void testCreateLibrarian() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Librarian User");
        em.persist(user);

        Librarian librarian = new Librarian();
        librarian.setUser(user);
        librarian.setEmploymentdate(LocalDate.of(2020, 1, 15));
        librarian.setPosition("Senior Librarian");
        em.persist(librarian);

        em.getTransaction().commit();

        Librarian retrievedLibrarian = em.find(Librarian.class, librarian.getId());
        assertNotNull(retrievedLibrarian, "The librarian should be persisted.");
        assertEquals(user.getId(), retrievedLibrarian.getUser().getId(), "The associated user should match.");
        assertEquals(LocalDate.of(2020, 1, 15), retrievedLibrarian.getEmploymentdate(), "The employment date should match.");
        assertEquals("Senior Librarian", retrievedLibrarian.getPosition(), "The position should match.");
    }

    @Test
    void testUpdateLibrarian() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Another Librarian");
        em.persist(user);

        Librarian librarian = new Librarian();
        librarian.setUser(user);
        librarian.setEmploymentdate(LocalDate.of(2018, 5, 20));
        librarian.setPosition("Junior Librarian");
        em.persist(librarian);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Librarian retrievedLibrarian = em.find(Librarian.class, librarian.getId());
        retrievedLibrarian.setPosition("Chief Librarian");
        em.getTransaction().commit();

        Librarian updatedLibrarian = em.find(Librarian.class, librarian.getId());
        assertEquals("Chief Librarian", updatedLibrarian.getPosition(), "The position should be updated.");
    }

    @Test
    void testDeleteLibrarian() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Temporary Librarian");
        em.persist(user);

        Librarian librarian = new Librarian();
        librarian.setUser(user);
        librarian.setEmploymentdate(LocalDate.of(2019, 3, 10));
        librarian.setPosition("Contract Librarian");
        em.persist(librarian);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Librarian retrievedLibrarian = em.find(Librarian.class, librarian.getId());
        em.remove(retrievedLibrarian);
        em.getTransaction().commit();

        Librarian deletedLibrarian = em.find(Librarian.class, librarian.getId());
        assertNull(deletedLibrarian, "The librarian should be deleted.");
    }

    @Test
    void testOneToOneUserToLibrarian() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("One-to-One Librarian User");
        em.persist(user);

        Librarian librarian = new Librarian();
        librarian.setUser(user);
        librarian.setEmploymentdate(LocalDate.of(2021, 7, 1));
        librarian.setPosition("Head Librarian");
        em.persist(librarian);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Librarian retrievedLibrarian = em.find(Librarian.class, librarian.getId());
        em.getTransaction().commit();

        assertNotNull(retrievedLibrarian.getUser(), "The librarian should be associated with a user.");
        assertEquals(user.getId(), retrievedLibrarian.getUser().getId(), "The associated user should match.");
    }
}
