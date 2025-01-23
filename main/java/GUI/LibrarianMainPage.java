package GUI;

import entity.Books;
import entity.Borrowing;
import entity.User;
import service.DatabaseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * GUI for librarian operations.
 * <p>
 * Provides an interface for librarians to manage books, users, and borrowings.
 * </p>
 */
public class LibrarianMainPage extends JFrame {
    private JPanel contentPane;
    private JButton newBorrowingButton;
    private JButton updateBorrowingButton;
    private JButton newBookButton;
    private JButton updateBookButton;
    private JButton newUserButton;
    private JButton updateUserButton;
    private JTable usersTable;
    private JTable booksTable;
    private JTable borrowingsTable;
    private JLabel usersLabel;
    private JLabel booksLabel;
    private JLabel borrowingsLabel;

    public LibrarianMainPage() {
        setContentPane(contentPane);
        setTitle("Main.Main page");
        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        setButtonFunctionality();

        populateTables();
    }

    private void setButtonFunctionality() {
        newBorrowingButton.addActionListener(e -> new BorrowingInfoWindow(this));

        updateBorrowingButton.addActionListener(e -> {
            int selectedRow = borrowingsTable.getSelectedRow();
            if (selectedRow != -1) {
                int borrowingId = (int) borrowingsTable.getValueAt(selectedRow, 0);
                new BorrowingInfoWindow(this, borrowingId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a borrowing to update.");
            }
        });

        newBookButton.addActionListener(e -> new BookInfoWindow(this));

        updateBookButton.addActionListener(e -> {

            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) booksTable.getValueAt(selectedRow, 0);
                new BookInfoWindow(this, bookId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to update.");
            }
        });
        newUserButton.addActionListener(e -> new UserInfoWindow(this));

        updateUserButton.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) usersTable.getValueAt(selectedRow, 0);
                new UserInfoWindow(this, userId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to update.");
            }
        });

    }

    /**
     * Populates the tables with data from the database.
     */
    public void populateTables() {
        DatabaseService dbService = new DatabaseService();

        try {
            DefaultTableModel usersTableModel = new DefaultTableModel(
                    new Object[]{"ID", "Name", "Email", "Phone", "Address"}, 0);
            usersTable.setModel(usersTableModel);

            DefaultTableModel booksTableModel = new DefaultTableModel(
                    new Object[]{"ID", "Title", "Author", "Publisher", "Year","ISBN", "Copies"}, 0);
            booksTable.setModel(booksTableModel);

            DefaultTableModel borrowingsTableModel = new DefaultTableModel(
                    new Object[]{"ID", "User", "Book Title", "Borrow Date", "Return Date"}, 0);
            borrowingsTable.setModel(borrowingsTableModel);

            List<User> users = dbService.getUsers();
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    Object[] row = {
                            user.getId(), user.getName(), user.getEmail(),
                            user.getPhoneNumber(), user.getAddress()
                    };
                    usersTableModel.addRow(row);
                }
            } else {
                System.out.println("No users found.");
            }

            List<Books> books = dbService.getBooks();
            booksTableModel.setRowCount(0);
            if (books != null && !books.isEmpty()) {
                for (Books book : books) {
                    Object[] row = {
                            book.getId(), book.getTitle(), book.getAuthor(),
                            book.getPublisher(), book.getPublicationYear(),
                            book.getIsbn(), book.getCopies().size()
                    };
                    booksTableModel.addRow(row);
                }
            } else {
                System.out.println("No books found.");
            }

            List<Borrowing> borrowings = dbService.getBorrowings();
            borrowingsTableModel.setRowCount(0);
            if (borrowings != null && !borrowings.isEmpty()) {
                for (Borrowing borrowing : borrowings) {
                    Object[] row = {
                            borrowing.getId(), borrowing.getUser().getName(),
                            borrowing.getCopy().getBook().getTitle(),
                            borrowing.getBorrowDate(), borrowing.getReturnDate()
                    };
                    borrowingsTableModel.addRow(row);
                }
            } else {
                System.out.println("No borrowings found.");
            }

            usersTable.revalidate();
            usersTable.repaint();
            booksTable.revalidate();
            booksTable.repaint();
            borrowingsTable.revalidate();
            borrowingsTable.repaint();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbService.close();
        }
    }
}
