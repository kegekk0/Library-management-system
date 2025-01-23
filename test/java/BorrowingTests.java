import entity.Books;
import entity.Borrowing;
import entity.User;
import entity.Copy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowingTests {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
    }

    @Test
    void testCreateBorrowing() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("John Doe");
        em.persist(user);

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now());
        em.persist(borrowing);

        em.getTransaction().commit();

        Borrowing retrievedBorrowing = em.find(Borrowing.class, borrowing.getId());
        assertNotNull(retrievedBorrowing, "The borrowing should be persisted.");
        assertEquals(user.getId(), retrievedBorrowing.getUser().getId(), "The user should match.");
        assertEquals(copy.getId(), retrievedBorrowing.getCopy().getId(), "The copy should match.");
    }

    @Test
    void testUpdateBorrowing() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Jane Doe");
        em.persist(user);

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now());
        em.persist(borrowing);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Borrowing retrievedBorrowing = em.find(Borrowing.class, borrowing.getId());
        retrievedBorrowing.setReturnDate(LocalDate.now().plusDays(7));
        em.getTransaction().commit();

        Borrowing updatedBorrowing = em.find(Borrowing.class, borrowing.getId());
        assertNotNull(updatedBorrowing.getReturnDate(), "The return date should be set.");
        assertEquals(LocalDate.now().plusDays(7), updatedBorrowing.getReturnDate(), "The return date should match.");
    }

    @Test
    void testDeleteBorrowing() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Alice Doe");
        em.persist(user);

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now());
        em.persist(borrowing);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Borrowing retrievedBorrowing = em.find(Borrowing.class, borrowing.getId());
        em.remove(retrievedBorrowing);
        em.getTransaction().commit();

        Borrowing deletedBorrowing = em.find(Borrowing.class, borrowing.getId());
        assertNull(deletedBorrowing, "The borrowing should be deleted.");
    }

    @Test
    void testOneToManyUserToBorrowings() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Charlie Doe");
        em.persist(user);

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy1 = new Copy();
        copy1.setBook(book);
        em.persist(copy1);

        Copy copy2 = new Copy();
        copy2.setBook(book);
        em.persist(copy2);

        Borrowing borrowing1 = new Borrowing();
        borrowing1.setUser(user);
        borrowing1.setCopy(copy1);
        borrowing1.setBorrowDate(LocalDate.now());
        user.getBorrowings().add(borrowing1);
        em.persist(borrowing1);

        Borrowing borrowing2 = new Borrowing();
        borrowing2.setUser(user);
        borrowing2.setCopy(copy2);
        borrowing2.setBorrowDate(LocalDate.now().plusDays(1));
        user.getBorrowings().add(borrowing2);
        em.persist(borrowing2);

        em.getTransaction().commit();

        em.getTransaction().begin();
        User retrievedUser = em.find(User.class, user.getId());
        em.getTransaction().commit();

        assertNotNull(retrievedUser.getBorrowings(), "The user should have borrowings.");
        assertEquals(2, retrievedUser.getBorrowings().size(), "The user should have two borrowings.");
    }

    @Test
    void testBorrowingWithNoReturnDate() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("David Doe");
        em.persist(user);

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        em.persist(copy);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setCopy(copy);
        borrowing.setBorrowDate(LocalDate.now());
        em.persist(borrowing);

        em.getTransaction().commit();

        Borrowing retrievedBorrowing = em.find(Borrowing.class, borrowing.getId());
        assertNull(retrievedBorrowing.getReturnDate(), "The return date should be null.");
    }

}
