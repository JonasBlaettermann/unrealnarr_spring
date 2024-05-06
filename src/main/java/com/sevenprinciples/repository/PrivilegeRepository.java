package com.sevenprinciples.repository;

import com.sevenprinciples.entity.Privilege;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends MongoRepository<Privilege, Long> {

    public Privilege findByName(String name);

}