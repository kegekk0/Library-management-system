import entity.User;
import entity.Borrowing;
import entity.Librarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
    }

    @Test
    void testCreateUser() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("123-456-7890");
        user.setAddress("123 Main.Main St");
        em.persist(user);

        em.getTransaction().commit();

        User retrievedUser = em.find(User.class, user.getId());
        assertNotNull(retrievedUser, "The user should be persisted.");
        assertEquals("John Doe", retrievedUser.getName(), "The name should match.");
        assertEquals("john.doe@example.com", retrievedUser.getEmail(), "The email should match.");
        assertEquals("123-456-7890", retrievedUser.getPhoneNumber(), "The phone number should match.");
        assertEquals("123 Main.Main St", retrievedUser.getAddress(), "The address should match.");
    }

    @Test
    void testUpdateUser() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPhoneNumber("987-654-3210");
        user.setAddress("456 Elm St");
        em.persist(user);

        em.getTransaction().commit();

        em.getTransaction().begin();
        User retrievedUser = em.find(User.class, user.getId());
        retrievedUser.setName("Jane Smith");
        retrievedUser.setAddress("789 Oak Ave");
        em.getTransaction().commit();

        User updatedUser = em.find(User.class, user.getId());
        assertEquals("Jane Smith", updatedUser.getName(), "The name should be updated.");
        assertEquals("789 Oak Ave", updatedUser.getAddress(), "The address should be updated.");
    }

    @Test
    void testDeleteUser() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Temporary User");
        user.setEmail("temp.user@example.com");
        user.setPhoneNumber("000-111-2222");
        user.setAddress("Temporary Address");
        em.persist(user);

        em.getTransaction().commit();

        em.getTransaction().begin();
        User retrievedUser = em.find(User.class, user.getId());
        em.remove(retrievedUser);
        em.getTransaction().commit();

        User deletedUser = em.find(User.class, user.getId());
        assertNull(deletedUser, "The user should be deleted.");
    }

    @Test
    void testOneToManyUserToBorrowings() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Borrowing User");
        user.setEmail("borrowing.user@example.com");
        user.setPhoneNumber("333-444-5555");
        user.setAddress("Borrowing Address");
        em.persist(user);

        Borrowing borrowing1 = new Borrowing();
        borrowing1.setUser(user);
        borrowing1.setBorrowDate(java.time.LocalDate.now());
        user.getBorrowings().add(borrowing1);
        em.persist(borrowing1);

        Borrowing borrowing2 = new Borrowing();
        borrowing2.setUser(user);
        borrowing2.setBorrowDate(java.time.LocalDate.now().plusDays(1));
        user.getBorrowings().add(borrowing2);
        em.persist(borrowing2);

        em.getTransaction().commit();

        em.getTransaction().begin();
        User retrievedUser = em.find(User.class, user.getId());
        em.getTransaction().commit();

        List<Borrowing> borrowings = retrievedUser.getBorrowings();
        assertNotNull(borrowings, "The user should have borrowings.");
        assertEquals(2, borrowings.size(), "The user should have two borrowings.");
    }

    @Test
    void testOneToManyUserToLibrarians() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Librarian User");
        user.setEmail("librarian.user@example.com");
        user.setPhoneNumber("666-777-8888");
        user.setAddress("Librarian Address");
        em.persist(user);

        Librarian librarian1 = new Librarian();
        librarian1.setUser(user);
        librarian1.setEmploymentdate(java.time.LocalDate.of(2020, 1, 1));
        librarian1.setPosition("Assistant Librarian");
        user.getLibrarians().add(librarian1);
        em.persist(librarian1);

        Librarian librarian2 = new Librarian();
        librarian2.setUser(user);
        librarian2.setEmploymentdate(java.time.LocalDate.of(2021, 1, 1));
        librarian2.setPosition("Head Librarian");
        user.getLibrarians().add(librarian2);
        em.persist(librarian2);

        em.getTransaction().commit();

        em.getTransaction().begin();
        User retrievedUser = em.find(User.class, user.getId());
        em.getTransaction().commit();

        List<Librarian> librarians = retrievedUser.getLibrarians();
        assertNotNull(librarians, "The user should have librarians.");
        assertEquals(2, librarians.size(), "The user should have two librarians.");
    }
}
