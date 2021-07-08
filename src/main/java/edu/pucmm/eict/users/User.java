package edu.pucmm.eict.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_USER_DELETEDAT", columnNames = {"username", "deletedat"}),
        @UniqueConstraint(name = "UQ_USER_EMAIL_DELETEDAT", columnNames = {"email", "deletedat"})})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<>();

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String lastname;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(columnDefinition = "char(64)", nullable = false)
    private String password;

    @Column(columnDefinition = "char(64)")
    private String secret;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long deletedAt;

    public User(String username, String email, String password, String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @PrePersist
    public void beforePersist() {
        this.joinedAt = LocalDateTime.now();
        this.deletedAt = 0L;
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}