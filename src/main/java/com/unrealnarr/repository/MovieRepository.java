package com.unrealnarr.repository;

import com.unrealnarr.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    public Optional<Movie> findById(String tconst);

    public Optional<Movie> findByTconst(String tconst);

    public List<Movie> findByTitleType(String titleType);

    public List<Movie> findByGenresContaining(String genre);

    public List<Movie> findByStartYearBetween(int startYear, int endYear);

}
