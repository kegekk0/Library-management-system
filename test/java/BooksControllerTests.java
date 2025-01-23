import controller.BooksController;
import entity.Books;
import entity.Publisher;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BooksControllerTests {

    private BooksController booksController;
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
        booksController = new BooksController();
    }

    @AfterAll
    void tearDown() {
        em.close();
        emf.close();
    }

    @BeforeEach
    void clearDatabase() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Books").executeUpdate();
        em.createQuery("DELETE FROM Publisher").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    void testSaveUser() {
        em.getTransaction().begin();
        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        em.persist(publisher);
        em.getTransaction().commit();

        booksController.saveUser("Test Title", "Test Author", publisher, 2023, "1234567890123");

        em.getTransaction().begin();
        List<Books> books = em.createQuery("SELECT b FROM Books b", Books.class).getResultList();
        System.out.println("Books in DB: " + books.size());
        books.forEach(book -> System.out.println("Book: " + book.getTitle()));
        em.getTransaction().commit();

        Books book = em.createQuery("SELECT b FROM Books b WHERE b.title = :title", Books.class)
                .setParameter("title", "Test Title")
                .getSingleResult();

        assertNotNull(book, "The book should be saved to the database.");
        assertEquals("Test Author", book.getAuthor(), "The author should match.");
    }


    @Test
    void testGetBookById() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        em.persist(publisher);

        Books book = new Books();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublisher(publisher);
        book.setPublicationYear(2023);
        book.setIsbn("1234567890123");
        em.persist(book);
        em.getTransaction().commit();

        em.getTransaction().begin();
        List<Books> books = em.createQuery("SELECT b FROM Books b", Books.class).getResultList();
        System.out.println("Books in DB after commit: " + books.size());
        books.forEach(b -> System.out.println("Book ID: " + b.getId() + ", Title: " + b.getTitle()));
        em.getTransaction().commit();


        Books retrievedBook = booksController.getBookById(book.getId());
        assertNotNull(retrievedBook, "The book should be retrieved by ID.");
        assertEquals(book.getTitle(), retrievedBook.getTitle(), "The title should match.");
    }

    @Test
    void testGetPublisherById() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        em.persist(publisher);

        em.getTransaction().commit();

        Publisher retrievedPublisher = booksController.getPublisherById(publisher.getId());
        assertNotNull(retrievedPublisher, "The publisher should be retrieved by ID.");
        assertEquals(publisher.getName(), retrievedPublisher.getName(), "The name should match.");
    }

    @Test
    void testUpdateBook() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        em.persist(publisher);

        Books book = new Books();
        book.setTitle("Original Title");
        book.setAuthor("Original Author");
        book.setPublisher(publisher);
        book.setPublicationYear(2020);
        book.setIsbn("9876543210987");
        em.persist(book);

        em.getTransaction().commit();

        Publisher newPublisher = new Publisher();
        newPublisher.setName("New Publisher");
        em.getTransaction().begin();
        em.persist(newPublisher);
        em.getTransaction().commit();

        booksController.updateBook(book.getId(), "Updated Title", "Updated Author", newPublisher, 2023, "1234567890123");

        Books updatedBook = em.find(Books.class, book.getId());
        assertNotNull(updatedBook, "The book should still exist after the update.");
        assertEquals("Updated Title", updatedBook.getTitle(), "The title should be updated.");
        assertEquals("Updated Author", updatedBook.getAuthor(), "The author should be updated.");
        assertEquals(newPublisher.getId(), updatedBook.getPublisher().getId(), "The publisher should be updated.");
        assertEquals(2023, updatedBook.getPublicationYear(), "The publication year should be updated.");
        assertEquals("1234567890123", updatedBook.getIsbn(), "The ISBN should be updated.");
    }
}
