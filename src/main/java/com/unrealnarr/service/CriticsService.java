package com.unrealnarr.service;

import com.unrealnarr.entity.Critic;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface CriticsService {

    public Critic findById(String id) throws Exception;
    public Collection<Critic> getCritics() throws Exception;

}

