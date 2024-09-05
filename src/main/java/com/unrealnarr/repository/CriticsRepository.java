package com.unrealnarr.repository;

import com.unrealnarr.entity.Critics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticsRepository extends MongoRepository<Critics, String> {

    public Critics findCriticById(String id);

}
