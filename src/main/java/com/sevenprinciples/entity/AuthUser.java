package com.sevenprinciples.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Collection;

@Data
@Builder
@Document("user")
public class AuthUser {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    @Indexed
    private String username;
    private String password;
    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}