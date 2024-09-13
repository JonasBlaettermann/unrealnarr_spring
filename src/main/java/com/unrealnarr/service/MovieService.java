package com.unrealnarr.service;

import com.unrealnarr.entity.Artist;
import com.unrealnarr.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface MovieService {

    public Movie findByTconst(String tconst) throws Exception;
    public Collection<Movie> getMovies() throws Exception;
    public void saveAll(List<Movie> artists) throws Exception;

}
