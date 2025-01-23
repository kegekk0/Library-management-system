import entity.Books;
import entity.Copy;
import entity.Borrowing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CopyTests {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
    }

    @Test
    void testCreateCopy() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        copy.setCopynumber(1);
        copy.setStatus("Available");
        em.persist(copy);

        em.getTransaction().commit();

        Copy retrievedCopy = em.find(Copy.class, copy.getId());
        assertNotNull(retrievedCopy, "The copy should be persisted.");
        assertEquals(book.getId(), retrievedCopy.getBook().getId(), "The book reference should match.");
        assertEquals(1, retrievedCopy.getCopynumber(), "The copy number should match.");
        assertEquals("Available", retrievedCopy.getStatus(), "The status should match.");
    }

    @Test
    void testUpdateCopy() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("1234567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        copy.setCopynumber(1);
        copy.setStatus("Available");
        em.persist(copy);
        em.getTransaction().commit();

        em.getTransaction().begin();
        Copy retrievedCopy = em.find(Copy.class, copy.getId());
        retrievedCopy.setStatus("Borrowed");
        em.getTransaction().commit();

        Copy updatedCopy = em.find(Copy.class, copy.getId());
        assertEquals("Borrowed", updatedCopy.getStatus(), "The status should be updated.");
    }

    @Test
    void testDeleteCopy() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("1234567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        copy.setCopynumber(1);
        copy.setStatus("Available");
        em.persist(copy);
        em.getTransaction().commit();

        em.getTransaction().begin();
        Copy retrievedCopy = em.find(Copy.class, copy.getId());
        em.remove(retrievedCopy);
        em.getTransaction().commit();

        Copy deletedCopy = em.find(Copy.class, copy.getId());
        assertNull(deletedCopy, "The copy should be deleted.");
    }

    @Test
    void testOneToManyCopyToBorrowings() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Sample Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        copy.setCopynumber(1);
        copy.setStatus("Available");
        em.persist(copy);

        Borrowing borrowing1 = new Borrowing();
        borrowing1.setCopy(copy);
        borrowing1.setBorrowDate(java.time.LocalDate.now());
        em.persist(borrowing1);

        Borrowing borrowing2 = new Borrowing();
        borrowing2.setCopy(copy);
        borrowing2.setBorrowDate(java.time.LocalDate.now().plusDays(1));
        em.persist(borrowing2);

        copy.getBorrowings().add(borrowing1);
        copy.getBorrowings().add(borrowing2);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Copy retrievedCopy = em.find(Copy.class, copy.getId());
        em.getTransaction().commit();

        List<Borrowing> borrowings = retrievedCopy.getBorrowings();
        assertNotNull(borrowings, "The copy should have borrowings.");
        assertEquals(2, borrowings.size(), "The copy should have two borrowings.");
    }

}
