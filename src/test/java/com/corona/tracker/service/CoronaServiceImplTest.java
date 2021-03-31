package com.corona.tracker.service;

import com.corona.tracker.domain.Corona;
import com.corona.tracker.repository.CoronaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


/**
 * created using diffblue plugin
 */

@ContextConfiguration(classes = {CoronaServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class CoronaServiceImplTest {
    @MockBean
    private CoronaRepository coronaRepository;

    @Autowired
    private CoronaServiceImpl coronaServiceImpl;

    @Test
    public void testSave() {
        Corona corona = new Corona();
        corona.setNumOfNewCases(10);
        corona.setTotalNumOfTested(10);
        corona.setTotalNumOfCases(10);
        corona.setNumOfActiveCases(10);
        corona.setNumOfNewlyRecovered(10);
        corona.setTotalNumOfDeaths(10);
        corona.setCountry("Country");
        corona.setId(123L);
        corona.setLastUpdate(new Timestamp(1L));
        corona.setNumOfCriticalCases(10);
        corona.setTotalNumOfRecovered(10);
        corona.setNumOfNewDeaths(10);
        when(this.coronaRepository.save((Corona) any())).thenReturn(corona);
        this.coronaServiceImpl.save(new Corona());
        verify(this.coronaRepository).save((Corona) any());
    }

    @Test
    public void testFindByLastUpdate() {
        ArrayList<Corona> coronaList = new ArrayList<Corona>();
        when(
                this.coronaRepository.findByLastUpdateBetween((java.time.LocalDateTime) any(), (java.time.LocalDateTime) any()))
                .thenReturn(coronaList);
        List<Corona> actualFindByLastUpdateResult = this.coronaServiceImpl.findByLastUpdate(LocalDate.ofEpochDay(1L));
        assertSame(coronaList, actualFindByLastUpdateResult);
        assertTrue(actualFindByLastUpdateResult.isEmpty());
        verify(this.coronaRepository).findByLastUpdateBetween((java.time.LocalDateTime) any(),
                (java.time.LocalDateTime) any());
    }

    @Test
    public void testFindAll() {
        ArrayList<Corona> coronaList = new ArrayList<Corona>();
        when(this.coronaRepository.findAll()).thenReturn(coronaList);
        List<Corona> actualFindAllResult = this.coronaServiceImpl.findAll();
        assertSame(coronaList, actualFindAllResult);
        assertTrue(actualFindAllResult.isEmpty());
        verify(this.coronaRepository).findAll();
    }
}

