package com.sevenprinciples.service;

import com.sevenprinciples.entity.Country;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface CountryService {

    public Country findById(String id) throws Exception;
    public void setCountry(Country country) throws Exception;
    public Collection<Country> getCountries() throws Exception;
    public void updateCountry(String id, Country country);
    public void deleteCountry(String id);
}
