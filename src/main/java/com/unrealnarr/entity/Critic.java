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
@Document(collection = "critic")
public class Critic {

    @Id
    private String id;
    private String movieTconst;
    private List<String> criticReviews;
}