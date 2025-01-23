package controller;

import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * Controller for managing user view.
 * <p>
 * Provides methods to create and update user view records.
 * </p>
 */public class UserViewController {
    private EntityManagerFactory entityManagerFactory;

    public UserViewController() {
        entityManagerFactory = PersistenceManager.getEntityManagerFactory();
    }

    public List<Object[]> getAllBooks() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT b.id, b.title, b.author, b.publicationyear " +
                        "FROM Books b", Object[].class);
        List<Object[]> results = query.getResultList();
        entityManager.close();
        return results;
    }

    public List<Object[]> getAllAvailableBooks() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT b.id, b.title, b.author " +
                        "FROM Books b JOIN b.copies c " +
                        "WHERE c.status = 'Available'", Object[].class);
        List<Object[]> results = query.getResultList();
        entityManager.close();
        return results;
    }

    public List<Object[]> getAllBooksBorrowedByUser(int userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            TypedQuery<Object[]> query = entityManager.createQuery(
                    "SELECT b.id, b.title, b.author, br.borrowdate, br.returndate " +
                            "FROM Borrowing br " +
                            "JOIN br.copy c " +
                            "JOIN c.book b " +
                            "WHERE br.user.id = :userId", Object[].class);
            query.setParameter("userId", userId);

            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

}
