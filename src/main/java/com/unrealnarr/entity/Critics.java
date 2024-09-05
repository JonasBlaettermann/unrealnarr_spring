package com.unrealnarr.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Critics {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    @CsvBindByName(column = "tconst")
    private String tconst;

    @CsvBindByName(column = "titleType")
    private String titleType;

    @CsvBindByName(column = "germanTitle")
    private String germanTitle;

    @CsvBindByName(column = "primaryTitle")
    private String primaryTitle;

    @CsvBindByName(column = "originalTitle")
    private String originalTitle;

    @CsvBindByName(column = "startYear")
    private int startYear;

    @CsvBindByName(column = "endYear")
    private int endYear;

    @CsvBindByName(column = "runtimeMinutes")
    private int runtimeMinutes;

    @CsvBindByName(column = "genres")
    private String genres;

    @CsvBindByName(column = "IMDBRating")
    private float IMDBRating;

    @CsvBindByName(column = "rating")
    private float rating;

    @CsvBindByName(column = "critic")
    private String[] critic;

    @CsvBindByName(column = "date")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date date;

    @CsvBindByName(column = "tags")
    private String[] tags;

    public Critics(String id, String tconst, String titleType, String germanTitle, String primaryTitle, String originalTitle, int startYear, int endYear, int runtimeMinutes, String genres, float IMDBRating, float rating, String[] critic, String[] tags, Date date) {
        this.tconst = tconst;
        this.titleType = titleType;
        this.germanTitle = germanTitle;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;
        this.IMDBRating = IMDBRating;
        this.rating = rating;
        this.critic = critic;
        this.tags = tags;
        this.date = date;
    }
}

