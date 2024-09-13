package com.unrealnarr.repository;

import com.unrealnarr.entity.Critic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticsRepository extends MongoRepository<Critic, String> {

    public Critic findCriticById(String id);

}
