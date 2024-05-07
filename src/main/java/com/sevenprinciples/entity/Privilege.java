package com.sevenprinciples.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(final String name) {
        super();
        this.name = name;
    }
}
