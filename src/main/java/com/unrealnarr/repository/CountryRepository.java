package com.unrealnarr.repository;

import com.unrealnarr.entity.Country;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {

    public Country findCountryById(String id);

}

