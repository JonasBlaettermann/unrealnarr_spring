package com.unrealnarr.repository;

import com.unrealnarr.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    public Movie findMoviesByTconst(String tconst);

    public List<Movie> findByTitleType(String titleType);

    public List<Movie> findByGenresContaining(String genre);

    public List<Movie> findByStartYearBetween(int startYear, int endYear);

}
