package com.unrealnarr.service;

import com.unrealnarr.entity.Artist;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public interface ArtistService {

    public Optional<Artist> findByNconst(String nconst) throws Exception;
    public Collection<Artist> getArtists() throws Exception;
    public void saveAll(List<Artist> artists) throws Exception;
}
