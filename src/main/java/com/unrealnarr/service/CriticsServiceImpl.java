package com.unrealnarr.service;

import com.unrealnarr.entity.Critics;
import com.unrealnarr.repository.CriticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public class CriticsServiceImpl implements CriticsService {

    @Autowired
    private CriticsRepository repository;

    @Override
    public Collection<Critics> getCritics() throws Exception {
        return repository.findAll();
    }

    @Override
    public Critics findById(String id) throws Exception {
        return repository.findCriticById(id);
    }

    @Override
    public void saveAll(List<Critics> criticsList) {
        repository.saveAll(criticsList);
    }

}