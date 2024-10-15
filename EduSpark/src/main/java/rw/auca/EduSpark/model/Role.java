package rw.auca.EduSpark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"userRoles", "menuRoles"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<MenuRole> menuRoles = new HashSet<>();
}