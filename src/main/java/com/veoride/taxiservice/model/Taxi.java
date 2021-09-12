package com.veoride.taxiservice.model;

import lombok.Data;

@Data
public class Taxi {

    private final String plate_number;  // taxi plate number
    
    private boolean available;          // if taxi is available to pick up customer
    private float lat;                  // current latitude of taxi location
    private float lng;                  // current longitude of taxi location
    
    public Taxi(String plate_number) {
        this.plate_number = plate_number;
        available = true;
        lat = 0;
        lng = 0;
   }
}
