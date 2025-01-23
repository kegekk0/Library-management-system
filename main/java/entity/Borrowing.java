package entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity representing borrowing transactions.
 * <p>
 * Links users to copies of books and records borrowing and return dates.
 * </p>
 */
@Entity
@Table(name = "BORROWINGS")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private entity.User user;

    @ManyToOne
    @JoinColumn(name = "COPY_ID")
    private entity.Copy copy;

    @Column(name = "BORROWDATE")
    private LocalDate borrowdate;

    @Column(name = "RETURNDATE")
    private LocalDate returndate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public entity.User getUser() {
        return user;
    }

    public void setUser(entity.User user) {
        this.user = user;
    }

    public entity.Copy getCopy() {
        return copy;
    }

    public void setCopy(entity.Copy copy) {
        this.copy = copy;
    }

    public LocalDate getBorrowDate() {
        return borrowdate;
    }

    public void setBorrowDate(LocalDate borrowdate) {
        this.borrowdate = borrowdate;
    }

    public LocalDate getReturnDate() {
        return returndate;
    }

    public void setReturnDate(LocalDate returndate) {
        this.returndate = returndate;
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "id=" + id +
                ", user=" + user +
                ", copy=" + copy +
                ", borrowdate=" + borrowdate +
                ", returndate=" + returndate +
                '}';
    }
}