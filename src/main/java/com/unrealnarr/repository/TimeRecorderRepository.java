package com.unrealnarr.repository;

import com.unrealnarr.entity.TimeRecorder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRecorderRepository extends MongoRepository<TimeRecorder, String> {

}
