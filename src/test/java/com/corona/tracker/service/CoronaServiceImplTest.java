package com.corona.tracker.service;

import com.corona.tracker.repository.CoronaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoronaServiceImplTest {
    //todo
    @Mock
    CoronaRepository coronaRepository;

    @Test
    void getCountryCoronaStats() {
    }

    @Test
    void populateDatabase() {
    }
}