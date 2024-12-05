package com.abernathyclinic.patientHistory.repository;

import com.abernathyclinic.patientHistory.model.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends MongoRepository<History, String> {

    List<History> findByPatId(int patId);

    @Override
    Optional<History> findById(String id);
}