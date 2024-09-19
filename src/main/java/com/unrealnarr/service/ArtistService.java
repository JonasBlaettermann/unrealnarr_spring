package com.unrealnarr.service;

import com.unrealnarr.entity.Artist;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface ArtistService {

    public Artist findByNconst(String nconst) throws Exception;

    public Collection<Artist> getArtists() throws Exception;

    public void saveAll(List<Artist> artists) throws Exception;

    public void updateArtists(List<Artist> artists) throws Exception;

}
