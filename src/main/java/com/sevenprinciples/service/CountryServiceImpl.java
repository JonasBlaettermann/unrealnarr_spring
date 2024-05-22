package com.sevenprinciples.service;

import com.sevenprinciples.entity.Country;
import com.sevenprinciples.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Collection<Country> getCountries() throws Exception {
        return countryRepository.findAll();
    }

    @Override
    public Country findById(String id) throws Exception {
        return countryRepository.findCountryById(id);
    }

    @Override
    public void setCountry(Country country) throws Exception {
        if (country != null) {
            countryRepository.save(country);
            return;
        } throw new Exception("Country is null");
    }

    @Override
    public void updateCountry(String id, Country country) {
        if (countryRepository.existsById(id)) {
            country.setId(id);
            countryRepository.save(country);
        } else {
            throw new IllegalArgumentException("Es existiert das Land mit der ID " + id + " nicht in der Datenbank");
        }
    }

    @Override
    public void deleteCountry(String id){
        if(countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Es existiert das Land mit der ID " + id + " nicht in der Datenbank");
        }
    }
}
