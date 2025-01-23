import controller.UserViewController;
import entity.Books;
import entity.Borrowing;
import entity.Copy;
import entity.User;
import org.junit.jupiter.api.*;
import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserViewControllerTests {

    private UserViewController userViewController;
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    void setUp() {
        emf = PersistenceManager.getEntityManagerFactory();
        em = emf.createEntityManager();
        userViewController = new UserViewController();
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
        em.createQuery("DELETE FROM Books").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    void testGetAllBooks() {
        em.getTransaction().begin();

        Books book1 = new Books();
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setPublicationYear(2021);
        book1.setIsbn("1234567890123");
        em.persist(book1);

        Books book2 = new Books();
        book2.setTitle("Book Two");
        book2.setAuthor("Author Two");
        book2.setPublicationYear(2022);
        book2.setIsbn("9876543210987");
        em.persist(book2);

        em.getTransaction().commit();

        List<Object[]> books = userViewController.getAllBooks();

        assertNotNull(books, "The list of books should not be null.");
        assertEquals(2, books.size(), "There should be two books in the database.");
    }

    @Test
    void testGetAllAvailableBooks() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Available Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("1234567890123");
        em.persist(book);

        Copy copy1 = new Copy();
        copy1.setBook(book);
        copy1.setStatus("Available");
        em.persist(copy1);

        Copy copy2 = new Copy();
        copy2.setBook(book);
        copy2.setStatus("Borrowed");
        em.persist(copy2);

        em.getTransaction().commit();

        List<Object[]> availableBooks = userViewController.getAllAvailableBooks();

        assertNotNull(availableBooks, "The list of available books should not be null.");
        assertEquals(1, availableBooks.size(), "There should be one available book.");
    }

    @Test
    void testGetAllBooksBorrowedByUser() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Test User");
        em.persist(user);

        Books book = new Books();
        book.setTitle("Borrowed Book");
        book.setAuthor("Author");
        book.setPublicationYear(2020);
        book.setIsbn("1111111111111");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now().minusDays(5));
        borrowing.setReturnDate(LocalDate.now().plusDays(5));
        em.persist(borrowing);

        em.getTransaction().commit();

        List<Object[]> borrowedBooks = userViewController.getAllBooksBorrowedByUser(user.getId().intValue());

        assertNotNull(borrowedBooks, "The list of borrowed books should not be null.");
        assertEquals(1, borrowedBooks.size(), "The user should have borrowed one book.");
        Object[] bookDetails = borrowedBooks.get(0);
        assertEquals("Borrowed Book", bookDetails[1], "The title of the borrowed book should match.");
    }
}
