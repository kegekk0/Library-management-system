package controller;

import entity.Books;
import entity.Publisher;
import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Controller for managing books.
 * <p>
 * Provides methods to create and update book records.
 * </p>
 */
public class BooksController {

    private EntityManagerFactory entityManagerFactory;

    public BooksController() {
        entityManagerFactory = PersistenceManager.getEntityManagerFactory();
    }

    /**
     * Creates a new book.
     *
     * @param title The title
     * @param author author
     * @param publisher publisher
     * @param year year
     * @param isbn isbn
     */
    public void saveUser(String title, String author, Publisher publisher, int year, String isbn) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Books newBook = new Books();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setPublisher(publisher);
        newBook.setPublicationYear(year);
        newBook.setIsbn(isbn);

        entityManager.persist(newBook);
        entityManager.flush();
        entityManager.getTransaction().commit();

        entityManager.close();

    }

    public Books getBookById(int bookId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Books book = entityManager.find(Books.class, bookId);
        entityManager.close();
        return book;
    }

    public Publisher getPublisherById(int publisherId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Publisher publisher = entityManager.find(Publisher.class, publisherId);
        entityManager.close();
        return publisher;
    }

    public void updateBook(int bookId, String title, String author, Publisher publisher, int year, String isbn) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Books book = entityManager.find(Books.class, bookId);
        if (book != null) {
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setPublicationYear(year);
            book.setIsbn(isbn);
        } else {
            System.out.println("Book with ID " + bookId + " not found!");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
