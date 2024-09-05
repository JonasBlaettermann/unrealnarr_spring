package com.unrealnarr.service;

import com.unrealnarr.entity.TimeRecorder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface TimeRecorderService {

    public void setTimeRecorder(TimeRecorder timeRecorder) throws Exception;
    public Collection<TimeRecorder> getTimeRecorder() throws Exception;
    public void updateTimeRecorder(String id, TimeRecorder timeRecorder);
}
