package com.unrealnarr.repository;

import com.unrealnarr.entity.Privilege;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends MongoRepository<Privilege, Long> {

    public Privilege findByName(String name);

}