package com.unrealnarr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Document(collection = "artist")
public class Artist {

    @Id
    private String nconst;
    private String primaryName;
    private List<RoleInMovie> rolesInMovies;

    @Setter
    @Getter
    public static class RoleInMovie {

        private String tconst;
        private int ordering;
        private String category;
        private String characters;
        private String job;
    }
}
