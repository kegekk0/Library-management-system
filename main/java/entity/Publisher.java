package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Entity representing publishers.
 * <p>
 * Links users to books.
 * </p>
 */
@Entity
@Table(name = "PUBLISHERS")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONENUMBER", length = 20)
    private String phonenumber;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Books> books = new ArrayList<>();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return getName();
    }
}