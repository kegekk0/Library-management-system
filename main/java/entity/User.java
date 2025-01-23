package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a user in the library system.
 * <p>
 * Includes personal information and borrowing records.
 * </p>
 */
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONENUMBER", length = 20)
    private String phonenumber;

    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Borrowing> borrowings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Librarian> librarians = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(List<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }

    public void setLibrarians(List<Librarian> librarians) {
        this.librarians = librarians;
    }

    @Override
    public String toString() {
        return "User {id=" + id + ", name='" + name + "', email='" + email + "', phoneNumber='" + phonenumber + "', address='" + address + "'}";
    }

}