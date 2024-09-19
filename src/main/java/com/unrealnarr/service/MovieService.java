package com.unrealnarr.service;

import com.unrealnarr.dto.MovieDTO;
import com.unrealnarr.entity.Artist;
import com.unrealnarr.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface MovieService {

    public Movie findMovieByTconst(String tconst) throws Exception;

    public Collection<Movie> getMovies() throws Exception;

    public Collection<MovieDTO> getMovieList() throws Exception;

    public void saveAll(List<Movie> movies) throws Exception;

    public void updateMovie(String tconst, Movie updatedMovie) throws Exception;

    public void updateMovies(List<Movie> movies) throws Exception;
}
