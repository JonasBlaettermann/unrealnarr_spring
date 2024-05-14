package com.sevenprinciples.service;

import com.sevenprinciples.entity.Protocol;
import com.sevenprinciples.repository.ProtocolRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ProtocolServiceImpl implements ProtocolService{

    @Autowired
    private ProtocolRepository repository;
    @Override
    public void addToProtocol(Protocol protocol) throws Exception{
        if(protocol!=null) {
            repository.save(protocol);
            return;
        } throw new Exception("Protocol is null");
    }

    @Override
    public Collection<Protocol> getProtocols() throws Exception {
        return repository.findAll();
    }


}
