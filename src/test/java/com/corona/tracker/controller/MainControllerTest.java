package com.corona.tracker.controller;

import com.corona.tracker.domain.Corona;
import com.corona.tracker.service.CoronaServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @InjectMocks
    MainController mainController;
    @Mock
    CoronaServiceImpl coronaService;
    @Mock
    Model model;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @AfterEach
    void tearDown() {
        reset(coronaService);
    }

    @Test
    void root() throws Exception {
        List<Corona> allStats = coronaService.getCountryCoronaStats();
        int totalReportedCases = allStats.stream().mapToInt(Corona::getTotalNumOfCases).sum();
        int totalNewCases = allStats.stream().mapToInt(Corona::getNumOfNewCases).sum();
        model.addAttribute("coronaData", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("coronaData", "totalReportedCases", "totalNewCases"))
                .andExpect(view().name("mainTemplateV2"));
    }
}