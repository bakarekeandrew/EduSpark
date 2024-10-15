package rw.auca.EduSpark.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "menus")
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer level;

    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent")
    private Set<Menu> children;

    @OneToMany(mappedBy = "menu")
    private Set<MenuRole> menuRoles;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Set<Menu> getChildren() {
        return children;
    }

    public void setChildren(Set<Menu> children) {
        this.children = children;
    }

    public Set<MenuRole> getMenuRoles() {
        return menuRoles;
    }

    public void setMenuRoles(Set<MenuRole> menuRoles) {
        this.menuRoles = menuRoles;
    }


    // Constructors, getters, and setters

}