package com.unrealnarr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
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
    private int runtimeInMinutes;  // Ensure this matches your TSV data
    private String genres;
    private float IMDBRating;
    private float rating;
    private Date date;

    @Field("tags")
    private List<String> tags;

    private List<Critic> critics;

    @Field("artistInfo")
    private List<Artist> artistInfo;

}
