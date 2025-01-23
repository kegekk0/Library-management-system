package service;

import entity.Books;
import entity.Borrowing;
import entity.User;

import javax.persistence.*;
import java.util.List;

/**
 * Service class for managing database interactions.
 * <p>
 * Provides methods to retrieve and manage users, books, and borrowings in the library system.
 * Utilizes JPA to handle persistence.
 * </p>
 */

public class DatabaseService {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public DatabaseService() {
        entityManagerFactory = PersistenceManager.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Fetches all users in the system.
     *
     * @return A list of {@link User} objects.
     */
    public List<User> getUsers() {
        String stringQuery = "SELECT u FROM User u";
        TypedQuery<User> query = entityManager.createQuery(stringQuery, User.class);
        return query.getResultList();
    }

    /**
     * Fetches all books available in the library.
     *
     * @return A list of {@link Books} objects.
     */
    public List<Books> getBooks() {
        String stringQuery = "SELECT b FROM Books b";
        TypedQuery<Books> query = entityManager.createQuery(stringQuery, Books.class);
        return query.getResultList();
    }

    /**
     * Retrieves all borrowings made by users.
     *
     * @return A list of {@link Borrowing} objects.
     */
    public List<Borrowing> getBorrowings() {
        String stringQuery = "SELECT b FROM Borrowing b";
        TypedQuery<Borrowing> query = entityManager.createQuery(stringQuery, Borrowing.class);
        return query.getResultList();
    }

    /**
     * Closes the database connections.
     */
    public void close() {
        entityManager.close();
        //entityManagerFactory.close();
    }
}
