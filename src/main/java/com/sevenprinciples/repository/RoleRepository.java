package com.sevenprinciples.repository;

import com.sevenprinciples.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, Long> {

    public Role findByName(String name);

}