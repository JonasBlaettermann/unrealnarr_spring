package com.unrealnarr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<AuthUser> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    public Role(final String name) {
        super();
        this.name = name;
    }
}
