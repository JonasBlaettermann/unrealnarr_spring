package com.sevenprinciples.repository;

import com.sevenprinciples.entity.Protocol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProtocolRepository extends MongoRepository<Protocol, Long> {

}