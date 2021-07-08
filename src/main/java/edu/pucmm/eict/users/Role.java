package edu.pucmm.eict.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Transient
    public static Role ADMIN = new Role(1, "ADMIN");

    @Transient
    public static Role APP_USER = new Role(2, "APP_USER");
}
