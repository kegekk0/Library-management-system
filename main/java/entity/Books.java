package entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;


/**
 * Entity class representing books.
 * <p>
 * Defines relationships with publishers and copies, and provides details about a book.
 * </p>
 */
@Entity
@Table(name = "BOOKS")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @ManyToOne
    @JoinColumn(name = "PUBLISHER_ID")
    private entity.Publisher publisher;

    @Column(name = "PUBLICATIONYEAR")
    private Integer publicationyear;

    @Column(name = "ISBN", length = 20)
    private String isbn;


    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Copy> copies = new LinkedList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public entity.Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationyear;
    }

    public void setPublicationYear(Integer publicationyear) {
        this.publicationyear = publicationyear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public void validateIsbn() {
        if (!isbn.matches("^[0-9]*$")) {
            throw new IllegalArgumentException("ISBN must contain only numeric characters.");
        }
    }


    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher=" + publisher +
                ", publicationyear=" + publicationyear +
                ", isbn='" + isbn + '\'' +
                ", copies=" + copies +
                '}';
    }
}