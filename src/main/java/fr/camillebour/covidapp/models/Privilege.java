package fr.camillebour.covidapp.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Privilege {

    public Privilege() {}

    public Privilege(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

}