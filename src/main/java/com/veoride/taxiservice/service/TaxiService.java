package com.veoride.taxiservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.CustomerRequest;
import com.veoride.taxiservice.model.Taxi;

import lombok.Getter;

@Getter
@Service
public class TaxiService {

    private Map<String, Taxi> taxis; // maps taxi plate number to taxi
    private Map<String, Customer> customers; // maps customer phone number to customer
    private Map<String, CustomerRequest> customerRequests; // map of customer requests.

    public TaxiService() {

        taxis = new HashMap<>();
        customers = new HashMap<>();
        customerRequests = new HashMap<>();
    }
}
