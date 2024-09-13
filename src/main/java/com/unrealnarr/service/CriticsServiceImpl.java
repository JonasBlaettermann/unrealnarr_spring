package com.unrealnarr.service;

import com.unrealnarr.entity.Critic;
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
    public Collection<Critic> getCritics() throws Exception {
        return repository.findAll();
    }

    @Override
    public Critic findById(String id) throws Exception {
        return repository.findCriticById(id);
    }


}