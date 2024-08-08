package com.sevenprinciples.repository;

import com.sevenprinciples.entity.Protocol;
import com.sevenprinciples.entity.TimeRecorder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRecorderRepository extends MongoRepository<TimeRecorder, String> {

}
