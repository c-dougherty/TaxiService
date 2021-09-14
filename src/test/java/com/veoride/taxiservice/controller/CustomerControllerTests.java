package com.veoride.taxiservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.CustomerRequest;
import com.veoride.taxiservice.model.Taxi;
import com.veoride.taxiservice.service.TaxiService;

import lombok.SneakyThrows;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaxiService taxiService;

    @SneakyThrows
    @Test
    public void testGetNearbyTaxis() {

        Map<String, Taxi> taxis = new HashMap<>();
        taxis.put("ABCDEFG", new Taxi("ABCDEFG", 0, 0));
        taxis.put("HIJKLMN", new Taxi("HIJKLMN", 5, 5));
        Mockito.when(taxiService.getTaxis()).thenReturn(taxis);

        mockMvc.perform(get("/api/taxi/nearby?distance=1&lat=5&lng=5"))
                .andExpect(jsonPath("$[0].plateNumber", is("HIJKLMN")));

        mockMvc.perform(get("/api/taxi/nearby?distance=1&lat=10&lng=10"))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    // TODO: Fix test.
    @Disabled
    @SneakyThrows
    @Test
    public void testSendRequestToTaxisTimeout() {

        Map<String, Customer> customers = new HashMap<>();
        customers.put("111-222-3333", new Customer("111-222-3333"));
        
        Map<String, Taxi> taxis = new HashMap<>();
        taxis.put("ABCDEFG", new Taxi("ABCDEFG"));
        
        Map<String, CustomerRequest> customerRequests = new HashMap<>();
        
        Mockito.when(taxiService.getCustomers()).thenReturn(customers);
        Mockito.when(taxiService.getTaxis()).thenReturn(taxis);
        Mockito.when(taxiService.getCustomerRequests()).thenReturn(customerRequests);
        
        List<String> plateNumbers = new ArrayList<>();
        plateNumbers.add("ABCDEFG");
        mockMvc.perform(post("/api/customer/111-222-3333/request").contentType(MediaType.APPLICATION_JSON).param("plateNumbers", plateNumbers.toString()))
                .andExpect(MockMvcResultMatchers.content().json("[null]"));
    }
}
