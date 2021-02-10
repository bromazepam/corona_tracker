package com.corona.tracker.service;

import com.corona.tracker.domain.Corona;
import com.corona.tracker.repository.CoronaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaServiceImpl implements CoronaService {

    private final CoronaRepository coronaRepository;
    private List<Corona> countryCoronaStats = new ArrayList<>();

    private static final String VIRUS_DATA_EUROPE = "https://corona.lmao.ninja/v3/covid-19/countries/AT, BE, BG, HR, CY, " +
            "CZ, DK, EE, FI, FR, DE, GR, HU, IE, IT, LV, LT, LU, MT, NL, PO, PT, RO,SK, SI,ES, SE, AL, AD, AM, BY, " +
            "BA, FO, GE,GI, IS, IM, LI,MK,MD, MC, ME, NO, RU, SM, RS, CH, TR,UA, GB, VA";
    private static final String VIRUS_DATA_SERBIA = "https://corona.lmao.ninja/v2/countries/serbia";
    private static final String VIRUS_DATA_WORLD = "https://corona.lmao.ninja/v3/covid-19/countries";

    @Override
    public void save(Corona corona) {
        coronaRepository.save(corona);
    }

    @Override
    public List<Corona> getCountryCoronaStats() {
        return countryCoronaStats;
    }

    @Override
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void populateDatabase() throws JsonProcessingException {
        List<Corona> tempListOfCoronaStats = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                " (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Object> response = restTemplate.exchange(VIRUS_DATA_EUROPE, HttpMethod.GET, entity, Object.class);

        int responseCode = response.getStatusCode().value();


        if (responseCode == 200) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(response.getBody());

            List<Object> list = mapper.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(List.class, Object.class));

            for (Object o : list) {
                Corona corona = new Corona();
                corona.setCountry(JsonPath.read(o, "$.country"));
                corona.setTotalNumOfCases(JsonPath.read(o, "$.cases"));
                corona.setNumOfActiveCases(JsonPath.read(o, "$.active"));
                corona.setNumOfNewCases(JsonPath.read(o, "$.todayCases"));
                corona.setNumOfCriticalCases(JsonPath.read(o, "$.critical"));
                corona.setTotalNumOfDeaths(JsonPath.read(o, "$.deaths"));
                corona.setNumOfNewDeaths(JsonPath.read(o, "$.todayDeaths"));
                corona.setTotalNumOfRecovered(JsonPath.read(o, "$.recovered"));
                corona.setNumOfNewlyRecovered(JsonPath.read(o, "$.todayRecovered"));
                corona.setLastUpdate(new Timestamp(JsonPath.read(o, "$.updated")));

                tempListOfCoronaStats.add(corona);
//            coronaRepository.save(corona);
            }

        }
        this.countryCoronaStats = tempListOfCoronaStats;
    }

    @Override
    public List<Corona> findByLastUpdate(LocalDate localDate) {
        return coronaRepository.findByLastUpdateBetween(LocalDateTime.of(localDate, LocalTime.MIN), LocalDateTime.of(localDate, LocalTime.MAX));
    }

    @Override
    public List<Corona> findAll() {
        return coronaRepository.findAll();
    }
}
