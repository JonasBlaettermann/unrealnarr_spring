package com.sevenprinciples.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class TimeRecorder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String username;
    private LocalDateTime start;
    private LocalDateTime end;

    private long durationInSeconds;


    public void setDuration(Duration duration) {
        this.durationInSeconds = duration.getSeconds();
    }

    public TimeRecorder(final LocalDateTime start) {
        super();
        this.start = start;
        this.end = null;
        this.durationInSeconds = 0L;
    }

    public TimeRecorder(final LocalDateTime start, final LocalDateTime end) {
        super();
        this.start = start;
        this.end = end;
        this.setDuration(Duration.between(start, end));
    }

}

