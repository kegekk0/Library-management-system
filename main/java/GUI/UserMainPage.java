package GUI;

import controller.UserViewController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;


/**
 * GUI for user operations.
 * <p>
 * Allows users to view books and their borrowing history.
 * </p>
 */
public class UserMainPage extends JFrame {
    private JPanel contentPane;
    private JTable allBooksTable;
    private JTable availableBooksTable;
    private JTable historyTable;
    private String username;

    public UserMainPage(String username) {
        this.username = username;
        setContentPane(contentPane);
        setTitle("Main.Main page for " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        populateTables();
    }

    /**
     * Populates the tables with user-specific data.
     */
    private void populateTables() {
        UserViewController userController = new UserViewController();

        int userId = Integer.parseInt(username.replace("user", ""));

        List<Object[]> allBooksData = userController.getAllBooks();
        List<Object[]> availableBooksData = userController.getAllAvailableBooks();
        List<Object[]> historyData = userController.getAllBooksBorrowedByUser(userId);

        populateTable(allBooksTable, allBooksData, new String[]{"Book ID", "Title", "Author", "Year"});
        populateTable(availableBooksTable, availableBooksData, new String[]{"Book ID", "Title", "Available"});
        populateTable(historyTable, historyData, new String[]{"Borrowing ID", "Book Title", "Borrow Date", "Return Date"});
    }

    private void populateTable(JTable table, List<Object[]> data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Object[] row : data) {
            model.addRow(row);
        }
        table.setModel(model);
    }
}
