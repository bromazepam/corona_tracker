package com.corona.tracker.controller;


import com.corona.tracker.service.CoronaServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    private final CoronaServiceImpl coronaService;

    @GetMapping("/")
    public String root(Model model) throws JsonProcessingException {
        coronaService.populateDatabase();

        return "mainTemplate";
    }

    @GetMapping("/2")
    public String root2(Model model) throws JsonProcessingException {

        model.addAttribute("corona", coronaService.findByLastUpdate(LocalDate.now().minusDays(1)));
        return "mainTemplate";
    }
}
