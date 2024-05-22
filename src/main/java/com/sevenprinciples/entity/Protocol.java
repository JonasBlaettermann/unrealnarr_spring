package com.sevenprinciples.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Protocol {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    private String action;
    private String username;
    private LocalDateTime timestamp;

    public Protocol(final String action, final String username) {
        super();
        this.action = action;
        this.username = username;
        this.timestamp = LocalDateTime.now();
    }
}
