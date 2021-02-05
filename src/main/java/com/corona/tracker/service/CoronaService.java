package com.corona.tracker.service;

import com.corona.tracker.domain.Corona;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.List;

public interface CoronaService {

    void save(Corona corona);

    void populateDatabase() throws JsonProcessingException;

    List<Corona> findByLastUpdate(LocalDate minusDays);

    List<Corona> findAll();
}
