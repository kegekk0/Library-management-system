package GUI;

import controller.BorrowingController;
import entity.Borrowing;
import entity.Copy;
import entity.User;
import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.LocalDate;

/**
 * GUI for managing borrowing information.
 * <p>
 * Enables librarians to add and update borrowing records.
 * </p>
 */
public class BorrowingInfoWindow extends JFrame {
    private JPanel contentPane;
    private JButton SAVEButton;
    private JButton CANCELButton;
    private JTextField copyText;
    private JTextField userText;
    private JLabel userLabel;
    private JLabel copyLabel;
    private JLabel borrowDateLabel;
    private JLabel returnedDateLabel;
    private JTextField returnedText;
    private JTextField borrowedText;
    private EntityManagerFactory entityManagerFactory = PersistenceManager.getEntityManagerFactory();
    BorrowingController borrowingController;

    public BorrowingInfoWindow(LibrarianMainPage librarianMainPage) {
        setContentPane(contentPane);
        setTitle("Borrowing Info");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        SAVEButton.addActionListener(e -> {

            int userId = Integer.parseInt(userText.getText().trim());
            int copyId = Integer.parseInt(copyText.getText().trim());
            LocalDate borrowDate = LocalDate.parse(borrowedText.getText().trim());
            LocalDate returnDate = returnedText.getText().trim().isEmpty() ? null : LocalDate.parse(returnedText.getText().trim());

            User user = fetchUserById(userId);
            Copy copy = fetchCopyById(copyId);

            if (user != null && copy != null) {
                borrowingController = new BorrowingController();
                borrowingController.saveBorrowing(user, copy, borrowDate, returnDate);

                librarianMainPage.populateTables();

                this.dispose();
                JOptionPane.showMessageDialog(this, "Borrowing Saved");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User or Copy ID");
            }
        });

        CANCELButton.addActionListener(e -> {
            dispose();
            entityManagerFactory.close();
        });
    }

    public BorrowingInfoWindow(LibrarianMainPage librarianMainPage, int borrowingId) {
        setContentPane(contentPane);
        setTitle("Update Borrowing Info");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Fetch borrowing by ID
        BorrowingController borrowingController = new BorrowingController();
        Borrowing borrowing = borrowingController.getBorrowingById(borrowingId);

        // Pre-fill text fields with the borrowing's data
        if (borrowing != null) {
            userText.setText(String.valueOf(borrowing.getUser().getId()));
            copyText.setText(String.valueOf(borrowing.getCopy().getId()));
            borrowedText.setText(borrowing.getBorrowDate().toString());
            if (borrowing.getReturnDate() != null) {
                returnedText.setText(borrowing.getReturnDate().toString());
            }
        }

        SAVEButton.addActionListener(e -> {
            int userId = Integer.parseInt(userText.getText().trim());
            int copyId = Integer.parseInt(copyText.getText().trim());
            LocalDate borrowDate = LocalDate.parse(borrowedText.getText().trim());
            LocalDate returnDate = returnedText.getText().trim().isEmpty()
                    ? null
                    : LocalDate.parse(returnedText.getText().trim());

            User user = borrowingController.fetchUserById(userId);
            Copy copy = borrowingController.fetchCopyById(copyId);

            if (user != null && copy != null) {
                // Update borrowing in the database
                borrowingController.updateBorrowing(borrowingId, user, copy, borrowDate, returnDate);

                librarianMainPage.populateTables(); // Refresh the table
                this.dispose();
                JOptionPane.showMessageDialog(this, "Borrowing Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User or Copy ID!");
            }
        });

        CANCELButton.addActionListener(e -> dispose());
    }




    private User fetchUserById(int userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.find(User.class, userId);
        entityManager.close();
        return user;
    }

    private Copy fetchCopyById(int copyId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Copy copy = entityManager.find(Copy.class, copyId);
        entityManager.close();
        return copy;
    }
}
