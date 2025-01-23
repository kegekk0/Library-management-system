package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Entity representing a copy of a book.
 * <p>
 * Each copy is linked to a specific book and tracks its availability.
 * </p>
 */
@Entity
@Table(name = "COPIES")
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Books book;

    @Column(name = "COPYNUMBER")
    private Integer copynumber;

    @Column(name = "STATUS", length = 20)
    private String status;

    @OneToMany(mappedBy = "copy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Borrowing> borrowings = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books books) {
        this.book = books;
    }

    public Integer getCopynumber() {
        return copynumber;
    }

    public void setCopynumber(Integer copynumber) {
        this.copynumber = copynumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(List<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }

}