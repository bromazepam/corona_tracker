package com.corona.tracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Corona {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String country;
    int numOfActiveCases;
    int numOfNewCases;
    int numOfCriticalCases;
    int numOfNewDeaths;
    int numOfNewlyRecovered;
    int totalNumOfRecovered;
    int totalNumOfDeaths;
    int totalNumOfCases;
    int totalNumOfTested;
    Timestamp lastUpdate;
}
