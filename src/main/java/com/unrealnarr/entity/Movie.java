package com.unrealnarr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "movie")
public class Movie {

    @Id
    private String tconst;

    private String titleType;
    private String germanTitle;
    private String primaryTitle;
    private String originalTitle;
    private int startYear;
    private int endYear;
    private int runtimeInMinutes;
    private String genres;
    private double IMDBRating;
    private double rating;
    private LocalDate date;

    @Field("tags")
    private List<String> tags;

    private String critics;

    @Field("artistInfo")
    private List<Artist> artistInfo;
}
