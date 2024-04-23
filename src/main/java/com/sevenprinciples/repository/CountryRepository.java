package com.sevenprinciples.repository;

import com.sevenprinciples.entity.Country;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {

    public Country findCountryById(String id);

}

