package com.sevenprinciples.service;

import com.sevenprinciples.entity.Protocol;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ProtocolService {

    public void addToProtocol(Protocol protocol) throws Exception;

    public Collection<Protocol> getProtocols() throws Exception;

}