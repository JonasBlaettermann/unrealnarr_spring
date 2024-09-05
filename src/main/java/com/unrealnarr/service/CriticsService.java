package com.unrealnarr.service;

import com.unrealnarr.entity.Critics;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface CriticsService {

    public Critics findById(String id) throws Exception;
    public Collection<Critics> getCritics() throws Exception;
    public void saveAll(List<Critics> criticsList) throws Exception;

}

