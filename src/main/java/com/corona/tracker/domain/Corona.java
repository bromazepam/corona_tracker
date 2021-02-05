package com.corona.tracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

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
    int active;
    int recovered;
    int cases;
    LocalDateTime lastUpdate;
}
