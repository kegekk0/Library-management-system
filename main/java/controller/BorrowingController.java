package controller;

import entity.Borrowing;
import entity.Copy;
import entity.User;
import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;


/**
 * Controller for managing borrowing operations.
 * <p>
 * Handles the creation, retrieval, and updating of borrowing records.
 * </p>
 */
public class BorrowingController {

    EntityManagerFactory entityManagerFactory;

    public BorrowingController() {
        entityManagerFactory = PersistenceManager.getEntityManagerFactory();
    }

    /**
     * Saves a new borrowing record.
     *
     * @param userID The user borrowing the book.
     * @param copyID The copy being borrowed.
     * @param borrowingDate The date of borrowing.
     * @param returningDate The expected return date.
     */
    public void saveBorrowing(User userID, Copy copyID, LocalDate borrowingDate, LocalDate returningDate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Borrowing newBorrowing = new Borrowing();
        newBorrowing.setUser(userID);
        newBorrowing.setCopy(copyID);
        newBorrowing.setBorrowDate(borrowingDate);
        newBorrowing.setReturnDate(returningDate);

        entityManager.persist(newBorrowing);
        entityManager.flush();
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();

    }

    /**
     * Retrieves a borrowing record by ID.
     *
     * @param borrowingId The ID of the borrowing record.
     * @return The borrowing record, or null if not found.
     */
    public Borrowing getBorrowingById(int borrowingId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Borrowing borrowing = entityManager.find(Borrowing.class, borrowingId);
        entityManager.close();
        return borrowing;
    }

    public User fetchUserById(int userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.find(User.class, userId);
        entityManager.close();
        return user;
    }

    public Copy fetchCopyById(int copyId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Copy copy = entityManager.find(Copy.class, copyId);
        entityManager.close();
        return copy;
    }

    public void updateBorrowing(int borrowingId, User user, Copy copy, LocalDate borrowDate, LocalDate returnDate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Borrowing borrowing = entityManager.find(Borrowing.class, borrowingId);
        if (borrowing != null) {
            borrowing.setUser(user);
            borrowing.setCopy(copy);
            borrowing.setBorrowDate(borrowDate);
            borrowing.setReturnDate(returnDate);
        } else {
            System.out.println("Borrowing with ID " + borrowingId + " not found!");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
