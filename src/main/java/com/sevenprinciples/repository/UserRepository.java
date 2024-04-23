package com.sevenprinciples.repository;

import com.sevenprinciples.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<AuthUser, String> {

    Optional<AuthUser> findByUsername(String username);

}
