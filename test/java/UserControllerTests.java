import controller.UserController;
import entity.User;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTests {

    private UserController userController;
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("testUnit");
        em = emf.createEntityManager();
        userController = new UserController();
    }

    @AfterAll
    void tearDown() {
        em.close();
        emf.close();
    }

    @BeforeEach
    void clearDatabase() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    void testSaveUser() {
        userController.saveUser("Test User", "testuser@example.com", "123-456-7890", "Test Address");

        em.getTransaction().begin();
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        System.out.println("Users in database: " + users.size());
        for (User user : users) {
            System.out.println("User: " + user.getName() + ", Email: " + user.getEmail());
        }
        em.getTransaction().commit();

        User savedUser = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", "testuser@example.com")
                .getSingleResult();

        assertNotNull(savedUser, "The user should be saved to the database.");
        assertEquals("Test User", savedUser.getName(), "The name should match.");
        assertEquals("testuser@example.com", savedUser.getEmail(), "The email should match.");
        assertEquals("123-456-7890", savedUser.getPhoneNumber(), "The phone number should match.");
        assertEquals("Test Address", savedUser.getAddress(), "The address should match.");
    }


    @Test
    void testGetUserById() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user.setPhoneNumber("123-456-7890");
        user.setAddress("Test Address");
        em.persist(user);

        em.getTransaction().commit();

        User retrievedUser = userController.getUserById(user.getId());
        assertNotNull(retrievedUser, "The user should be retrieved by ID.");
        assertEquals(user.getName(), retrievedUser.getName(), "The name should match.");
        assertEquals(user.getEmail(), retrievedUser.getEmail(), "The email should match.");
    }

    @Test
    void testUpdateUser() {
        em.getTransaction().begin();

        User user = new User();
        user.setName("Original Name");
        user.setEmail("original@example.com");
        user.setPhoneNumber("111-111-1111");
        user.setAddress("Original Address");
        em.persist(user);

        em.getTransaction().commit();

        userController.updateUser(user.getId(), "Updated Name", "updated@example.com", "222-222-2222", "Updated Address");

        User updatedUser = em.find(User.class, user.getId());
        assertNotNull(updatedUser, "The user should still exist after the update.");
        assertEquals("Updated Name", updatedUser.getName(), "The name should be updated.");
        assertEquals("updated@example.com", updatedUser.getEmail(), "The email should be updated.");
        assertEquals("222-222-2222", updatedUser.getPhoneNumber(), "The phone number should be updated.");
        assertEquals("Updated Address", updatedUser.getAddress(), "The address should be updated.");
    }

    @Test
    void testGetNonExistentUser() {
        User retrievedUser = userController.getUserById(9999);
        assertNull(retrievedUser, "The user should not exist.");
    }
}
