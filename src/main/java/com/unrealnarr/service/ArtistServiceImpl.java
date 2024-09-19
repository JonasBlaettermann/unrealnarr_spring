package com.unrealnarr.service;

import com.mongodb.client.result.UpdateResult;
import com.unrealnarr.entity.Artist;
import com.unrealnarr.repository.ArtistRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private static final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final MongoTemplate mongoTemplate;
    @Autowired
    private ArtistRepository repository;

    @Autowired
    public ArtistServiceImpl(MongoTemplate mongoTemplate, ArtistRepository repository) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    @Override
    public Collection<Artist> getArtists() throws Exception {
        return repository.findAll();
    }

    @Override
    public Artist findByNconst(String tconst) throws Exception {
        Optional<Artist> artist = repository.findById(tconst);
        if (artist.isPresent()) {
            logger.info("Fetched Movie with tconst : " + tconst);
            return artist.get();
        } else {
            logger.warn("Movie with tconst : " + tconst + " doesn't exist");
            throw new Exception("Movie with tconst : " + tconst + " doesn't exist");
        }
    }

    @Override
    public void updateArtists(List<Artist> artists) throws Exception {
        for (Artist artist : artists) {
            Query query = new Query(Criteria.where("nconst").is(artist.getNconst()));
            Update update = new Update()
                    .set("primaryName", artist.getPrimaryName())
                    .set("roleInMovie", artist.getRolesInMovies());

            try {
                UpdateResult result = mongoTemplate.updateFirst(query, update, Artist.class);

                if (result.getMatchedCount() == 0) {
                    logger.info("A new data entry will be created for this artist with nconst: " + artist.getNconst());
                    repository.save(artist);
                } else if (result.getModifiedCount() > 0) {
                    logger.info("Successfully updated artist with nconst: " + artist.getNconst());
                } else {
                    logger.info("No changes made to artist with nconst: " + artist.getNconst());
                }
            } catch (Exception e) {
                logger.error("Error updating Artist with nconst: " + artist.getNconst(), e);
                throw new RuntimeException("Error updating Artist with nconst: " + artist.getNconst(), e);
            }
        }
    }

    @Override
    public void saveAll(List<Artist> artists) throws Exception {
        Set<String> ids = artists.stream()
                .map(Artist::getNconst)
                .collect(Collectors.toSet());

        List<Artist> existingArtists = ids.stream()
                .map(id -> repository.findByNconst(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        Set<String> existingIds = existingArtists.stream()
                .map(Artist::getNconst)
                .collect(Collectors.toSet());

        List<Artist> toSave = artists.stream()
                .filter(artist -> !existingIds.contains(artist.getNconst()))
                .collect(Collectors.toList());

        repository.saveAll(toSave);
    }

}
