package com.sevenprinciples.service;

import com.sevenprinciples.entity.Country;
import com.sevenprinciples.entity.TimeRecorder;
import com.sevenprinciples.repository.CountryRepository;
import com.sevenprinciples.repository.TimeRecorderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TimeRecorderServiceImpl implements TimeRecorderService {

    @Autowired
    private TimeRecorderRepository repository;


    @Override
    public Collection<TimeRecorder> getTimeRecorder() throws Exception {
        return repository.findAll();
    }


    @Override
    public void setTimeRecorder(TimeRecorder timeRecorder) throws Exception {
        if (timeRecorder != null) {
            repository.save(timeRecorder);
            return;
        } throw new Exception("TimeRecorder is null");
    }

    @Override
    public void updateTimeRecorder(String id, TimeRecorder timeRecorder) {
        if (repository.existsById(id)) {
            timeRecorder.setId(id);
            repository.save(timeRecorder);
        } else {
            throw new IllegalArgumentException("Es existiert der TimeRecorder mit der ID " + id + " nicht in der Datenbank");
        }
    }

}