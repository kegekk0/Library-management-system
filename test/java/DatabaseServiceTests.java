import entity.Books;
import entity.Borrowing;
import entity.User;
import org.junit.jupiter.api.*;
import service.DatabaseService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseServiceTests {

    private DatabaseService databaseService;
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();

        databaseService = new DatabaseService();
        overrideEntityManagerForTesting(databaseService, em);
    }

    private void overrideEntityManagerForTesting(DatabaseService service, EntityManager testEntityManager) {
        try {
            Field entityManagerField = DatabaseService.class.getDeclaredField("entityManager");
            entityManagerField.setAccessible(true);
            entityManagerField.set(service, testEntityManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to override EntityManager in DatabaseService", e);
        }
    }


    @AfterAll
    void tearDown() {
        databaseService.close();
        em.close();
        emf.close();
    }

    @BeforeEach
    public void insertTestData() {
        em.getTransaction().begin();

        User user1 = new User();
        user1.setName("User One");
        em.persist(user1);

        User user2 = new User();
        user2.setName("User Two");
        em.persist(user2);

        Books book1 = new Books();
        book1.setTitle("Book One");
        em.persist(book1);

        Books book2 = new Books();
        book2.setTitle("Book Two");
        em.persist(book2);

        Borrowing borrowing1 = new Borrowing();
        borrowing1.setUser(user1);
        borrowing1.setBorrowDate(java.time.LocalDate.now());
        em.persist(borrowing1);

        Borrowing borrowing2 = new Borrowing();
        borrowing2.setUser(user2);
        borrowing2.setBorrowDate(java.time.LocalDate.now().plusDays(1));
        em.persist(borrowing2);

        em.getTransaction().commit();
    }




    @Test
    void testGetUsers() {
        List<User> users = databaseService.getUsers();

        assertNotNull(users, "Users list should not be null.");
        assertEquals(2, users.size(), "There should be two users in the database.");
        assertTrue(users.stream().anyMatch(user -> "User One".equals(user.getName())), "User One should exist.");
        assertTrue(users.stream().anyMatch(user -> "User Two".equals(user.getName())), "User Two should exist.");
    }

    @Test
    void testGetBooks() {
        List<Books> books = databaseService.getBooks();

        assertNotNull(books, "Books list should not be null.");
        assertEquals(2, books.size(), "There should be two books in the database.");
        assertTrue(books.stream().anyMatch(book -> "Book One".equals(book.getTitle())), "Book One should exist.");
        assertTrue(books.stream().anyMatch(book -> "Book Two".equals(book.getTitle())), "Book Two should exist.");
    }

    @Test
    void testGetBorrowings() {
        List<Borrowing> borrowings = databaseService.getBorrowings();

        assertNotNull(borrowings, "Borrowings list should not be null.");
        assertEquals(2, borrowings.size(), "There should be two borrowings in the database.");
        assertTrue(borrowings.stream().anyMatch(borrowing -> borrowing.getUser().getName().equals("User One")), "Borrowing by User One should exist.");
        assertTrue(borrowings.stream().anyMatch(borrowing -> borrowing.getUser().getName().equals("User Two")), "Borrowing by User Two should exist.");
    }
}
