package com.unrealnarr.repository;

import com.unrealnarr.entity.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {

    public Optional<Artist> findById(String nconst);

    public Optional<Artist> findByNconst(String nconst);

}

