package com.corona.tracker.domain;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class CoronaTest {

    @Test
    void groupedAssertions() {
        Corona corona = new Corona(1L, "country", 1, 1, 1,
                1, 1, 1, 1, 1,
                1, any(Timestamp.class));

        assertAll("test corona",
                () -> assertEquals(corona.getId(), 1L, "id failed"),
                () -> assertEquals(corona.getCountry(), "country", "name of the country failed"),
                () -> assertEquals(corona.getNumOfActiveCases(), 1, "number of active cases failed"),
                () -> assertEquals(corona.getNumOfCriticalCases(), 1, "number of critical cases failed"),
                () -> assertEquals(corona.getNumOfNewCases(), 1, "number of new cases failed"),
                () -> assertEquals(corona.getNumOfNewDeaths(), 1, " number of today's deaths failed"),
                () -> assertEquals(corona.getNumOfNewlyRecovered(), 1, "number of newly recovered failed"),
                () -> assertEquals(corona.getTotalNumOfCases(), 1, "total number of cases failed"),
                () -> assertEquals(corona.getTotalNumOfDeaths(), 1, "number of deaths failed"),
                () -> assertEquals(corona.getTotalNumOfRecovered(), 1, "total number of recovered failed"),
                () -> assertEquals(corona.getTotalNumOfTested(), 1, "total number of tested failed"),
                () -> assertEquals(corona.getLastUpdate(), any(), "timestamp failed"));
    }

}