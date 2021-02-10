package com.corona.tracker.service;

import com.corona.tracker.domain.Corona;
import com.corona.tracker.repository.CoronaRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jayway.jsonpath.JsonPath;
import com.mysql.cj.xdevapi.JsonArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.hibernate.sql.OracleJoinFragment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaServiceImpl implements CoronaService {

    private final CoronaRepository coronaRepository;
    private List<Corona> allStats = new ArrayList<>();
    private static String VIRUS_DATA_EUROPE = "https://corona.lmao.ninja/v3/covid-19/countries/AL, AD,AT, BY, BE, BA," +
            " BG, JE, CH, CZ, DE, DK, EE, ES, FO, FI, FR, GB, GI, GR, HU, HR,IE, IS, IT, LI, LT, LU, LV, MC, MK, MT, " +
            "NO, NL, PL,PT, RO, RU, SE, SI, SK, SM, UA, VA";

    private static String VIRUS_DATA_SERBIA = "https://corona.lmao.ninja/v2/countries/serbia";
    @Override
    public void save(Corona corona) {
        coronaRepository.save(corona);
    }

    @Override
    public List<Corona> getAllStats() {
        return allStats;
    }
    @Override
    @PostConstruct
    public void populateDatabase() throws JsonProcessingException {
        List<Corona> tempList = new ArrayList<>();
        Corona corona = new Corona();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                " (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<Object> response = restTemplate.exchange(VIRUS_DATA_EUROPE, HttpMethod.GET, entity, Object.class);

        int responseCode = response.getStatusCode().value();


        if (responseCode == 200) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(response.getBody());

            List<Object> list = mapper.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(List.class, Object.class));

            for(Object o : list){
//                log.info(o.toString());
                corona.setNumOfActiveCases(JsonPath.read(o, "$.active"));
                corona.setTotalNumOfRecovered(JsonPath.read(o, "$.recovered"));
                corona.setTotalNumOfCases(JsonPath.read(o, "$.cases"));
                corona.setCountry(JsonPath.read(o, "$.country"));
                corona.setLastUpdate(new Timestamp(JsonPath.read(o, "$.updated")));
                tempList.add(corona);
                log.info(corona.toString());
//            coronaRepository.save(corona);
            }

        }
        this.allStats = tempList;
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
