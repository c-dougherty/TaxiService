package com.veoride.taxiservice.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Taxi {

    private final String plateNumber; // taxi plate number

    private final Map<String, CustomerRequest> customerRequests; // map of customer phone number to customer request (for this taxi only)
    
    private TaxiStatus status;
    
    public Taxi(String plateNumber, TaxiStatus status) {
    	
    	this.plateNumber = plateNumber;
        customerRequests = new HashMap<>();
        this.status = status;
    }
}
