package com.sevenprinciples.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

import javax.persistence.Id;

@Data
@Builder
@Document("user")
public class AuthUser {
    @Id
    private  ObjectId id;
    @Indexed
    private String username;
    private String password;
    private boolean active;
}