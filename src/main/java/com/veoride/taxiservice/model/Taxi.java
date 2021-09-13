package com.veoride.taxiservice.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Taxi {

    private final String plateNumber; // taxi plate number

    private Map<String, CustomerRequest> customerRequests; // map of customer phone number to customer request (for this taxi only)
    private boolean available; // if taxi is available to pick up customer
    private float lat; // current latitude of taxi location
    private float lng; // current longitude of taxi location

    public Taxi(String plateNumber) {

        this(plateNumber, 0, 0);
    }

    public Taxi(String plateNumber, float lat, float lng) {

        this.plateNumber = plateNumber;
        customerRequests = new HashMap<>();
        available = true;
        this.lat = lat;
        this.lng = lng;
    }
}
