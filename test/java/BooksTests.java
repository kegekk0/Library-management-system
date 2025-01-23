import entity.Books;
import entity.Copy;
import entity.Publisher;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class BooksTests {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
    }

    @Test
    void testCreateBook() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2023);
        book.setIsbn("1234567890123");

        em.persist(book);
        em.getTransaction().commit();

        Books retrievedBook = em.find(Books.class, book.getId());
        assertNotNull(retrievedBook, "The book should not be null");
        assertEquals("Test Book", retrievedBook.getTitle(), "The book title should match");
        assertEquals("Test Author", retrievedBook.getAuthor(), "The book author should match");

        em.close();
    }

    @Test
    void testDatabasaConnection(){
        assertDoesNotThrow(() -> {
            em.getTransaction().begin();
            em.getTransaction().commit();
        });
    }

    @Test
    void testUpdateBook() {

        em.getTransaction().begin();
        Books book = new Books();
        book.setTitle("Old Title");
        book.setAuthor("Old Author");
        book.setPublicationYear(2022);
        book.setIsbn("987-6543210987");
        em.persist(book);
        em.getTransaction().commit();

        em.getTransaction().begin();
        Books persistedBook = em.find(Books.class, book.getId());
        persistedBook.setTitle("New Title");
        persistedBook.setAuthor("New Author");
        em.getTransaction().commit();

        Books updatedBook = em.find(Books.class, book.getId());
        assertNotNull(updatedBook, "The updated book should not be null");
        assertEquals("New Title", updatedBook.getTitle(), "The title should be updated");
        assertEquals("New Author", updatedBook.getAuthor(), "The author should be updated");
    }


    @Test
    void testDeleteBook() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("To Be Deleted");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("111-2223334445");

        em.persist(book);
        em.getTransaction().commit();

        em.getTransaction().begin();
        book = em.find(Books.class, book.getId());
        em.remove(book);
        em.getTransaction().commit();

        Books deletedBook = em.find(Books.class, book.getId());
        assertNull(deletedBook, "The book should be null after deletion");

        em.close();
    }

    @Test
    void testDeleteBookReferencedByCopy() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Referenced Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        em.persist(book);

        Copy copy = new Copy();
        copy.setBook(book);
        em.persist(copy);

        em.getTransaction().commit();

        em.getTransaction().begin();
        em.remove(book);
        Exception exception = assertThrows(Exception.class, em::flush);
        em.getTransaction().rollback();

        assertNotNull(exception, "Deleting a referenced book should fail.");
    }

    @Test
    void testOneToManyBooksToCopies() {
        em.getTransaction().begin();

        Books book = new Books();
        book.setTitle("Book with Copies");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("1234567890123");
        em.persist(book);

        Copy copy1 = new Copy();
        copy1.setBook(book);
        em.persist(copy1);

        Copy copy2 = new Copy();
        copy2.setBook(book);
        em.persist(copy2);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Books retrievedBook = em.find(Books.class, book.getId());
        Hibernate.initialize(retrievedBook.getCopies());
        em.getTransaction().commit();

        assertNotNull(retrievedBook.getCopies(), "The copies collection should not be null.");
        assertEquals(2, retrievedBook.getCopies().size(), "The book should have two copies.");
    }

    @Test
    void testManyToOneBooksToPublisher() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        em.persist(publisher);

        Books book = new Books();
        book.setTitle("Book with Publisher");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("123-4567890123");
        book.setPublisher(publisher);
        em.persist(book);

        em.getTransaction().commit();

        Books retrievedBook = em.find(Books.class, book.getId());
        assertNotNull(retrievedBook.getPublisher());
        assertEquals("Test Publisher", retrievedBook.getPublisher().getName());
    }

    @Test
    void testInvalidIsbnFormat() {
        Books book = new Books();
        book.setTitle("Invalid ISBN Book");
        book.setAuthor("Author");
        book.setPublicationYear(2021);
        book.setIsbn("INVALID_ISBN");

        assertThrows(IllegalArgumentException.class, () -> {
            book.validateIsbn();
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        });
    }


}


