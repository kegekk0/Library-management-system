import controller.BorrowingController;
import entity.Borrowing;
import entity.Copy;
import entity.User;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BorrowingControllerTests {

    private BorrowingController borrowingController;
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
        borrowingController = new BorrowingController();
    }

    @AfterAll
    void tearDown() {
        em.close();
        emf.close();
    }

    @BeforeEach
    void clearDatabase() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Borrowing").executeUpdate();
        em.createQuery("DELETE FROM Copy").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    void testSaveBorrowing() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Test User");
        em.persist(user);

        Copy copy = new Copy();
        em.persist(copy);

        em.getTransaction().commit();

        borrowingController.saveBorrowing(user, copy, LocalDate.now(), LocalDate.now().plusDays(7));

        Borrowing borrowing = em.createQuery("SELECT b FROM Borrowing b", Borrowing.class).getSingleResult();

        assertNotNull(borrowing, "Borrowing should be saved in the database.");
        assertEquals(user.getId(), borrowing.getUser().getId(), "The user should match.");
        assertEquals(copy.getId(), borrowing.getCopy().getId(), "The copy should match.");
        assertEquals(LocalDate.now(), borrowing.getBorrowDate(), "The borrowing date should match.");
        assertEquals(LocalDate.now().plusDays(7), borrowing.getReturnDate(), "The returning date should match.");
    }

    @Test
    void testGetBorrowingById() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Test User");
        em.persist(user);

        Copy copy = new Copy();
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setReturnDate(LocalDate.now().plusDays(7));
        em.persist(borrowing);

        em.getTransaction().commit();

        Borrowing retrievedBorrowing = borrowingController.getBorrowingById(borrowing.getId());
        assertNotNull(retrievedBorrowing, "The borrowing should be retrieved by ID.");
        assertEquals(borrowing.getUser().getId(), retrievedBorrowing.getUser().getId(), "The user should match.");
    }

    @Test
    void testFetchUserById() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Test User");
        em.persist(user);

        em.getTransaction().commit();

        User retrievedUser = borrowingController.fetchUserById(user.getId());
        assertNotNull(retrievedUser, "The user should be retrieved by ID.");
        assertEquals(user.getName(), retrievedUser.getName(), "The name should match.");
    }

    @Test
    void testFetchCopyById() {
        em.getTransaction().begin();

        Copy copy = new Copy();
        em.persist(copy);

        em.getTransaction().commit();

        Copy retrievedCopy = borrowingController.fetchCopyById(copy.getId());
        assertNotNull(retrievedCopy, "The copy should be retrieved by ID.");
    }

    @Test
    void testUpdateBorrowing() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Test User");
        em.persist(user);

        Copy copy = new Copy();
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setReturnDate(LocalDate.now().plusDays(7));
        em.persist(borrowing);

        em.getTransaction().commit();

        User newUser = new User();
        newUser.setName("Updated User");
        em.getTransaction().begin();
        em.persist(newUser);
        em.getTransaction().commit();

        borrowingController.updateBorrowing(borrowing.getId(), newUser, copy, LocalDate.now().plusDays(1), LocalDate.now().plusDays(8));

        Borrowing updatedBorrowing = em.find(Borrowing.class, borrowing.getId());
        assertNotNull(updatedBorrowing, "The borrowing should still exist after the update.");
        assertEquals(newUser.getId(), updatedBorrowing.getUser().getId(), "The user should be updated.");
        assertEquals(LocalDate.now().plusDays(1), updatedBorrowing.getBorrowDate(), "The borrowing date should be updated.");
        assertEquals(LocalDate.now().plusDays(8), updatedBorrowing.getReturnDate(), "The returning date should be updated.");
    }
}
