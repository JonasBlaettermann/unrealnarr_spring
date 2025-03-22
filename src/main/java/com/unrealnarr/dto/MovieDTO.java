package com.unrealnarr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
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
    private List<String> tags;
    private List<ArtistDTO> artistInfo;
}
