package com.unrealnarr.service;

import com.unrealnarr.entity.Artist;
import com.unrealnarr.repository.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository repository;

    @Override
    public Collection<Artist> getArtists() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Artist> findByNconst(String nconst) throws Exception {
        return repository.findByNconst(nconst);
    }

    @Override
    public void saveAll(List<Artist> artists) throws Exception {
        Set<String> ids = artists.stream()
                .map(Artist::getNconst)
                .collect(Collectors.toSet());

        List<Artist> existingArtists = ids.stream()
                .map(id -> repository.findByNconst(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<String> existingIds = existingArtists.stream()
                .map(Artist::getNconst)
                .collect(Collectors.toSet());

        List<Artist> toSave = artists.stream()
                .filter(artist -> !existingIds.contains(artist.getNconst()))
                .collect(Collectors.toList());

        repository.saveAll(toSave);
    }
}
