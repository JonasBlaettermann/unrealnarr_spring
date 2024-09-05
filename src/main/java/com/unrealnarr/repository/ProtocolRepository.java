package com.unrealnarr.repository;

import com.unrealnarr.entity.Protocol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProtocolRepository extends MongoRepository<Protocol, Long> {

}