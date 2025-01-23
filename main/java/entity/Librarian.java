package entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity representing librarians.
 * <p>
 * Links users to positions.
 * </p>
 */
@Entity
@Table(name = "LIBRARIANS")
public class Librarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private entity.User user;

    @Column(name = "EMPLOYMENTDATE")
    private LocalDate employmentdate;

    @Column(name = "POSITION")
    private String position;

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

    public LocalDate getEmploymentdate() {
        return employmentdate;
    }

    public void setEmploymentdate(LocalDate employmentdate) {
        this.employmentdate = employmentdate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}