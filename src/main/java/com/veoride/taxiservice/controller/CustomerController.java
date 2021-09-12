package com.veoride.taxiservice.controller;

import com.veoride.taxiservice.model.Taxi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    
    private List<Taxi> taxiList;
    
    public CustomerController(List<Taxi> taxiList) {
        this.taxiList = taxiList;
    }

    @GetMapping("api/taxi/nearby")
    public ResponseEntity<List<Taxi>> getNearbyTaxis(float distance, float lat, float lng) {
        
        // TODO: Return list of available taxis within distance of customer
        ResponseEntity<List<Taxi>> resp = new ResponseEntity<List<Taxi>>(taxiList, HttpStatus.OK);
        return resp;
    }
    
    @PostMapping("api/customer/{phone_number}/request")
    public ResponseEntity<String> sendCustomerRequestToTaxis(@PathVariable("phone_number") String phone_number, List<Taxi> taxiList) {
        
        // TODO: Get plate_number of taxi who accepts request, or return null if all taxis deny or if request not accepted after 1 minute
        ResponseEntity<String> resp = new ResponseEntity<String>("plate_number", HttpStatus.OK);
        return resp;
    }
}
