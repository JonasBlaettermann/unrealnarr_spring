package com.unrealnarr.service;

import com.unrealnarr.dto.MovieDTO;
import com.unrealnarr.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface MovieService {

    Movie findMovieByTconst(String tconst) throws Exception;

    Collection<Movie> getMovies() throws Exception;

    Page<MovieDTO> getMovieList(Pageable pageable) throws Exception;

    void saveAll(List<Movie> movies) throws Exception;

    void updateMovie(String tconst, Movie updatedMovie) throws Exception;

    void updateMovies(List<Movie> movies) throws Exception;

    Page<MovieDTO> searchMovies(String search, Pageable pageable) throws Exception;
}
