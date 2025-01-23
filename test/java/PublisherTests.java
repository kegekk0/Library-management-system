import entity.Publisher;
import entity.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class PublisherTests {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
    }

    @Test
    void testCreatePublisher() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Sample Publisher");
        publisher.setAddress("123 Main.Main St");
        publisher.setPhonenumber("123-456-7890");
        em.persist(publisher);

        em.getTransaction().commit();

        Publisher retrievedPublisher = em.find(Publisher.class, publisher.getId());
        assertNotNull(retrievedPublisher, "The publisher should be persisted.");
        assertEquals("Sample Publisher", retrievedPublisher.getName(), "The name should match.");
        assertEquals("123 Main.Main St", retrievedPublisher.getAddress(), "The address should match.");
        assertEquals("123-456-7890", retrievedPublisher.getPhonenumber(), "The phone number should match.");
    }

    @Test
    void testUpdatePublisher() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Old Publisher");
        publisher.setAddress("456 Old Rd");
        publisher.setPhonenumber("987-654-3210");
        em.persist(publisher);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Publisher retrievedPublisher = em.find(Publisher.class, publisher.getId());
        retrievedPublisher.setName("Updated Publisher");
        retrievedPublisher.setAddress("789 New Ave");
        em.getTransaction().commit();

        Publisher updatedPublisher = em.find(Publisher.class, publisher.getId());
        assertEquals("Updated Publisher", updatedPublisher.getName(), "The name should be updated.");
        assertEquals("789 New Ave", updatedPublisher.getAddress(), "The address should be updated.");
    }

    @Test
    void testDeletePublisher() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Temporary Publisher");
        publisher.setAddress("Temporary Address");
        publisher.setPhonenumber("000-111-2222");
        em.persist(publisher);

        em.getTransaction().commit();

        em.getTransaction().begin();
        Publisher retrievedPublisher = em.find(Publisher.class, publisher.getId());
        em.remove(retrievedPublisher);
        em.getTransaction().commit();

        Publisher deletedPublisher = em.find(Publisher.class, publisher.getId());
        assertNull(deletedPublisher, "The publisher should be deleted.");
    }

    @Test
    void testOneToManyPublisherToBooks() {
        em.getTransaction().begin();

        Publisher publisher = new Publisher();
        publisher.setName("Book Publisher");
        publisher.setAddress("Book Publisher Address");
        publisher.setPhonenumber("555-555-5555");
        em.persist(publisher);

        Books book1 = new Books();
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setPublicationYear(2021);
        book1.setIsbn("111-1111111111");
        book1.setPublisher(publisher);
        publisher.getBooks().add(book1);
        em.persist(book1);

        Books book2 = new Books();
        book2.setTitle("Book Two");
        book2.setAuthor("Author Two");
        book2.setPublicationYear(2022);
        book2.setIsbn("222-2222222222");
        book2.setPublisher(publisher);
        publisher.getBooks().add(book2);
        em.persist(book2);

        em.getTransaction().commit();

        Publisher retrievedPublisher = em.find(Publisher.class, publisher.getId());
        assertNotNull(retrievedPublisher.getBooks(), "The publisher should have books.");
        assertEquals(2, retrievedPublisher.getBooks().size(), "The publisher should have two books.");
    }
}
