package com.veoride.taxiservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.veoride.taxiservice.model.Taxi;
import com.veoride.taxiservice.service.TaxiService;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaxiService taxiService;

    @Before
    public void before() {

        taxiService.getTaxis().put("ABCDEFG", new Taxi("ABCDEFG", 0, 0));
        taxiService.getTaxis().put("HIJKLMN", new Taxi("HIJKLMN", 5, 5));
    }

    @Ignore
    @Test
    public void testGetNearbyTaxis() {

        // TODO: Implement test
        List<Taxi> expectedTaxisNearby = new ArrayList<>();
        expectedTaxisNearby.add(new Taxi("HIJKLMN", 5, 5));
        // mockMvc.perform(MockMvcRequestBuilders.get("api/taxi/nearby", 1, 5,
        // 5)).andExpect(MockMvcResultMatchers.content().equals(expectedTaxisNearby));
    }
}
