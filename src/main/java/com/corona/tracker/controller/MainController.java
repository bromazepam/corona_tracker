package com.corona.tracker.controller;


import com.corona.tracker.domain.Corona;
import com.corona.tracker.service.CoronaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final CoronaServiceImpl coronaService;

    @GetMapping("/")
    public String root(Model model) {
        List<Corona> allStats = coronaService.getCountryCoronaStats();
        int totalReportedCases = allStats.stream().mapToInt(Corona::getTotalNumOfCases).sum();
        int totalNewCases = allStats.stream().mapToInt(Corona::getNumOfNewCases).sum();
        model.addAttribute("coronaData", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "mainTemplateV2";
    }
}
