package com.unrealnarr.service;

import com.unrealnarr.entity.Artist;
import com.unrealnarr.entity.Movie;
import com.unrealnarr.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository repository;

    @Override
    public Movie findByTconst(String tconst) throws Exception {
        return null;
    }

    @Override
    public Collection<Movie> getMovies() throws Exception {
        return List.of();
    }

    @Override
    public void saveAll(List<Movie> movies) {
        repository.saveAll(movies);
    }
}
