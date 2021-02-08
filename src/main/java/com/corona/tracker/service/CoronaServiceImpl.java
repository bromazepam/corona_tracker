package com.corona.tracker.service;

import com.corona.tracker.domain.Corona;
import com.corona.tracker.repository.CoronaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaServiceImpl implements CoronaService {

    private final CoronaRepository coronaRepository;

    @Override
    public void save(Corona corona){
        coronaRepository.save(corona);
    }

    @Override
    public void populateDatabase() throws JsonProcessingException {
        final String uri = "https://corona.lmao.ninja/v2/countries/serbia";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                " (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

//        Object obj = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class).getBody();
        ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class);

        int responseCode = response.getStatusCode().value();

        log.info(String.valueOf(responseCode));
        if(responseCode == 200){
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(response.getBody());

            Corona corona = new Corona();
            corona.setNumOfActiveCases(JsonPath.read(jsonString, "$.active"));
            corona.setTotalNumOfRecovered(JsonPath.read(jsonString, "$.recovered"));
            corona.setTotalNumOfCases(JsonPath.read(jsonString, "$.cases"));
            corona.setCountry(JsonPath.read(jsonString, "$.country"));

            Timestamp ts=new Timestamp(JsonPath.read(jsonString, "$.updated"));
            Date date=new Date(ts.getTime());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            System.out.println(date);
            corona.setLastUpdate(String.valueOf(date));
            log.info(String.valueOf(date));
            log.info(corona.toString());
            coronaRepository.save(corona);
        }
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
