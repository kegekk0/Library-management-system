package GUI;

import controller.UserController;
import entity.User;

import javax.swing.*;

/**
 * GUI for managing user information.
 * <p>
 * Enables librarians to add and update user details.
 * </p>
 */
public class UserInfoWindow extends JFrame {

    private UserController userController;
    private JButton SAVEButton;
    private JPanel contentPane;
    private JTextField nameText;
    private JTextField emailText;
    private JTextField phoneText;
    private JTextField addressText;
    private JButton CANCELButton;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneNumberLabel;
    private JLabel addressLabel;

    public UserInfoWindow(LibrarianMainPage librarianMainPage) {

        setContentPane(contentPane);
        setTitle("Enter user Information");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        SAVEButton.addActionListener(e -> {

            userController = new UserController();

            String name = nameText.getText().trim();
            String email = emailText.getText().trim();
            String phone = phoneText.getText().trim();
            String address = addressText.getText().trim();

            userController.saveUser(name, email, phone, address);
            librarianMainPage.populateTables();

            this.dispose();

            JOptionPane.showMessageDialog(this, "User Saved");
        });

        CANCELButton.addActionListener(e -> {
            dispose();
        });
    }

    public UserInfoWindow(LibrarianMainPage librarianMainPage, int userId) {
        setContentPane(contentPane);
        setTitle("Update User Info");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Fetch user by ID
        UserController userController = new UserController();
        User user = userController.getUserById(userId);

        // Pre-fill text fields with the user's data
        if (user != null) {
            nameText.setText(user.getName());
            emailText.setText(user.getEmail());
            phoneText.setText(user.getPhoneNumber());
            addressText.setText(user.getAddress());
        }

        SAVEButton.addActionListener(e -> {
            String name = nameText.getText().trim();
            String email = emailText.getText().trim();
            String phone = phoneText.getText().trim();
            String address = addressText.getText().trim();

            // Update user in the database
            userController.updateUser(userId, name, email, phone, address);

            librarianMainPage.populateTables(); // Refresh the table
            this.dispose();
            JOptionPane.showMessageDialog(this, "User Updated Successfully!");
        });

        CANCELButton.addActionListener(e -> dispose());
    }


}
