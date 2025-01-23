package GUI;

import javax.swing.*;
import java.io.InputStream;
import java.util.Properties;

/**
 * GUI for logging in.
 * <p>
 * Provides an interface for users to log into their account.
 * </p>
 */
public class LoginScreen extends JFrame {

    private JPanel contentPane;
    private JPasswordField passwordTextField;
    private JFormattedTextField usernameTextField;
    private JButton loginButton;

    public LoginScreen() {
        setContentPane(contentPane);
        setTitle("Login Screen");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        loginButton.addActionListener(e -> {
            try {
                Properties loginInfo = new Properties();
                try (InputStream input = getClass().getClassLoader().getResourceAsStream("login_info.properties")) {
                    if (input == null) {
                        throw new IllegalArgumentException("Login info file not found");
                    }
                    loginInfo.load(input);
                }

                String username = usernameTextField.getText().trim();
                String password = new String(passwordTextField.getPassword()).trim();

                if (validateLogin(username, password, loginInfo)) {
                    if (username.startsWith("user")) {
                        int userId = Integer.parseInt(username.replace("user", ""));
                        new UserMainPage(String.valueOf(userId));
                    } else if (username.startsWith("lib")) {
                        new LibrarianMainPage();
                    }
                    this.dispose();
                } else {
                    throw new Exception("Invalid username or password");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean validateLogin(String username, String password, Properties loginInfo) {
        for (String key : loginInfo.stringPropertyNames()) {
            if (key.endsWith("login") && loginInfo.getProperty(key).equals(username)) {
                String passwordKey = key.replace("login", "password");
                String correctPassword = loginInfo.getProperty(passwordKey);
                return correctPassword != null && correctPassword.equals(password);
            }
        }
        return false;
    }
}
