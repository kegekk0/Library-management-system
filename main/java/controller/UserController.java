package controller;

import entity.User;
import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Controller for managing users.
 * <p>
 * Provides methods to create and update user records.
 * </p>
 */
public class UserController {
    private EntityManagerFactory entityManagerFactory;


    public UserController() {
        entityManagerFactory = PersistenceManager.getEntityManagerFactory();
    }

    /**
     * Creates a new user.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param phone The user's phone number.
     * @param address The user's address.
     */
    public void saveUser(String name, String email, String phone, String address) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phone);
        newUser.setAddress(address);

        entityManager.persist(newUser);
        entityManager.flush();
        entityManager.getTransaction().commit();

        entityManager.close();

    }

    public User getUserById(int userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.find(User.class, userId);
        entityManager.close();
        return user;
    }

    /**
     * Updates an existing user's information.
     *
     * @param userId The ID of the user to update.
     * @param name The updated name.
     * @param email The updated email address.
     * @param phone The updated phone number.
     * @param address The updated address.
     */
    public void updateUser(int userId, String name, String email, String phone, String address) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            user.setAddress(address);
        } else {
            System.out.println("User with ID " + userId + " not found!");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
