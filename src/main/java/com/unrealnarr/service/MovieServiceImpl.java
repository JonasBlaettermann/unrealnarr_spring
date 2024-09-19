package com.unrealnarr.service;

import com.unrealnarr.dto.ArtistDTO;
import com.unrealnarr.dto.MovieDTO;
import com.unrealnarr.entity.Movie;
import com.unrealnarr.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.mongodb.client.result.UpdateResult;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MongoTemplate mongoTemplate;
    private final MovieRepository repository;

    @Autowired
    public MovieServiceImpl(MongoTemplate mongoTemplate, MovieRepository repository) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    @Override
    public Movie findMovieByTconst(String nconst) throws Exception {
        Optional<Movie> movie = repository.findById(nconst);
        if (movie.isPresent()) {
            logger.info("Fetched Artist with nconst : " + nconst);
            return movie.get();
        } else {
            logger.warn("Artist with nconst : " + nconst + " doesn't exist");
            throw new Exception("Artist with nconst : " + nconst + " doesn't exist");
        }
    }

    @Override
    public Collection<Movie> getMovies() throws Exception {
        return repository.findAll();
    }

    @Override
    public Collection<MovieDTO> getMovieList() throws Exception {
        List<Movie> movies = repository.findAll();
        return movies.stream()
                .map(movie -> {
                    MovieDTO movieDTO = new MovieDTO();
                    movieDTO.setTconst(movie.getTconst());
                    movieDTO.setTitleType(movie.getTitleType());
                    movieDTO.setGermanTitle(movie.getGermanTitle());
                    movieDTO.setPrimaryTitle(movie.getPrimaryTitle());
                    movieDTO.setOriginalTitle(movie.getOriginalTitle());
                    movieDTO.setStartYear(movie.getStartYear());
                    movieDTO.setEndYear(movie.getEndYear());
                    movieDTO.setRuntimeInMinutes(movie.getRuntimeInMinutes());
                    movieDTO.setGenres(movie.getGenres());
                    movieDTO.setIMDBRating(movie.getIMDBRating());
                    movieDTO.setRating(movie.getRating());
                    movieDTO.setDate(movie.getDate());
                    movieDTO.setTags(movie.getTags());
                    movieDTO.setArtistInfo(movie.getArtistInfo().stream()
                            .map(artist -> {
                                ArtistDTO artistDTO = new ArtistDTO();
                                artistDTO.setNconst(artist.getNconst());
                                artistDTO.setPrimaryName(artist.getPrimaryName());
                                return artistDTO;
                            })
                            .collect(Collectors.toList()));
                    return movieDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Movie> movies) throws Exception {

        Set<String> ids = movies.stream()
                .map(Movie::getTconst)
                .collect(Collectors.toSet());

        List<Movie> existingMovies = ids.stream()
                .map(id -> repository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        Set<String> existingIds = existingMovies.stream()
                .map(Movie::getTconst)
                .collect(Collectors.toSet());

        List<Movie> toSave = movies.stream()
                .filter(movie -> !existingIds.contains(movie.getTconst()))
                .collect(Collectors.toList());

        repository.saveAll(toSave);
    }

    @Override
    public void updateMovie(String tconst, Movie movie) throws Exception {

        Query query = new Query(Criteria.where("tconst").is(tconst));
        Update update = new Update()
                .set("primaryTitle", movie.getPrimaryTitle())
                .set("originalTitle", movie.getOriginalTitle())
                .set("startYear", movie.getStartYear())
                .set("endYear", movie.getEndYear())
                .set("genres", movie.getGenres())
                .set("IMDBRating", movie.getIMDBRating())
                .set("rating", movie.getRating())
                .set("runtimeInMinutes", movie.getRuntimeInMinutes())
                .set("tags", movie.getTags())
                .set("critics", movie.getCritics());

        try {
            UpdateResult result = mongoTemplate.updateFirst(query, update, Movie.class);

            if (result.getMatchedCount() == 0) {
                logger.warn("Movie not found with tconst: " + movie.getTconst());
                logger.info("A new data entry will be created for this movie");
                repository.save(movie);
            } else if (result.getModifiedCount() > 0) {
                logger.info("Successfully updated movie with tconst: " + movie.getTconst());
            } else {
                logger.info("No changes made to movie with tconst: " + movie.getTconst());
            }
        } catch (Exception e) {
            logger.error("Error updating movie with tconst: " + movie.getTconst(), e);
            throw new RuntimeException("Error updating movie with tconst: " + movie.getTconst(), e);
        }
    }

    @Override
    public void updateMovies(List<Movie> movies) throws Exception {
        for (Movie movie : movies) {
            logger.info("Processing movie with tconst: " + movie.getTconst());

            Query query = new Query(Criteria.where("tconst").is(movie.getTconst()));
            Update update = new Update()
                    .set("primaryTitle", movie.getPrimaryTitle())
                    .set("originalTitle", movie.getOriginalTitle())
                    .set("startYear", movie.getStartYear())
                    .set("endYear", movie.getEndYear())
                    .set("genres", movie.getGenres())
                    .set("IMDBRating", movie.getIMDBRating())
                    .set("rating", movie.getRating())
                    .set("runtimeInMinutes", movie.getRuntimeInMinutes())
                    .set("tags", movie.getTags())
                    .set("critics", movie.getCritics());

            try {
                UpdateResult result = mongoTemplate.updateFirst(query, update, Movie.class);

                if (result.getMatchedCount() == 0) {
                    logger.warn("Movie not found with tconst: " + movie.getTconst());
                    logger.info("A new data entry will be created for this movie");
                    repository.save(movie);
                } else if (result.getModifiedCount() > 0) {
                    logger.info("Successfully updated movie with tconst: " + movie.getTconst());
                } else {
                    logger.info("No changes made to movie with tconst: " + movie.getTconst());
                }
            } catch (Exception e) {
                logger.error("Error updating movie with tconst: " + movie.getTconst(), e);
                throw new RuntimeException("Error updating movie with tconst: " + movie.getTconst(), e);
            }
        }
    }
}