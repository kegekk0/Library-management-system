package GUI;

import controller.BooksController;
import entity.Books;
import entity.Publisher;
import service.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;

/**
 * GUI for managing book information.
 * <p>
 * Enables librarians to add and update book details.
 * </p>
 */
public class BookInfoWindow extends JFrame {
    private JPanel contentPane;
    private JTextField isbnText;
    private JTextField titleText;
    private JTextField authorText;
    private JTextField yearText;
    private JTextField publisherText;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel isbnLabel;
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JLabel yearLabel;
    private JLabel publisherLabel;
    private EntityManagerFactory entityManagerFactory = PersistenceManager.getEntityManagerFactory();
    BooksController booksController;


    public BookInfoWindow(LibrarianMainPage librarianMainPage) {
        setContentPane(contentPane);
        setTitle("Main.Main page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        saveButton.addActionListener(e -> {

            booksController = new BooksController();

            String title = titleText.getText().trim();
            String author = authorText.getText().trim();
            int publisherId = Integer.parseInt(publisherText.getText().trim());
            int year = Integer.parseInt(yearText.getText().trim());
            String isbn = isbnText.getText().trim();

            Publisher publisher = fetchPublisherById(publisherId);

            booksController.saveUser(title, author, publisher, year, isbn);
            librarianMainPage.populateTables();

            this.dispose();

            JOptionPane.showMessageDialog(this, "Book Saved");
        });
        cancelButton.addActionListener(e -> {
            dispose();
            entityManagerFactory.close();
        });
    }

    public BookInfoWindow(LibrarianMainPage librarianMainPage, int bookId) {
        setContentPane(contentPane);
        setTitle("Update Book Info");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Fetch the book by ID
        BooksController booksController = new BooksController();
        Books book = booksController.getBookById(bookId);

        // Pre-fill text fields with the book's data
        if (book != null) {
            titleText.setText(book.getTitle());
            authorText.setText(book.getAuthor());
            publisherText.setText(String.valueOf(book.getPublisher().getId()));
            yearText.setText(String.valueOf(book.getPublicationYear()));
            isbnText.setText(book.getIsbn());
        }

        saveButton.addActionListener(e -> {
            String title = titleText.getText().trim();
            String author = authorText.getText().trim();
            int publisherId = Integer.parseInt(publisherText.getText().trim());
            int year = Integer.parseInt(yearText.getText().trim());
            String isbn = isbnText.getText().trim();

            Publisher publisher = booksController.getPublisherById(publisherId);

            booksController.updateBook(bookId, title, author, publisher, year, isbn);

            librarianMainPage.populateTables();
            this.dispose();
            JOptionPane.showMessageDialog(this, "Book Updated");
        });

        cancelButton.addActionListener(e -> dispose());
    }


    private Publisher fetchPublisherById(int publisherId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Publisher publisher = entityManager.find(Publisher.class, publisherId);
        entityManager.close();
        return publisher;
    }

}
