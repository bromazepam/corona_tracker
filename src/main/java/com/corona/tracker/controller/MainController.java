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
    public String root2(Model model) {
        List<Corona> allStats = coronaService.getAllStats();
        model.addAttribute("coronaData", allStats);
        return "mainTemplate";
    }
}
