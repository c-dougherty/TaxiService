package com.veoride.taxiservice.controller;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.Taxi;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TaxiController {
    
    private List<Taxi> taxiList;
    private List<Customer> customerList;
    
    public TaxiController(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @PatchMapping("api/taxi/{plate_number}")
    public void updateTaxiStatus(@PathVariable("plate_number") String plate_number, boolean available, float lat, float lng) {
        
        // TODO: Update taxi status
    }
    
    @GetMapping("/api/taxi/{plate_number}/requests")
    public ResponseEntity<List<Customer>> getRequestsMadeToTaxi(@PathVariable("plate_number") String plate_number) {
        
        // TODO: Get list of customers who have made requests to taxi with plate_number
        // Create response payload {"customer": []}
        List<Customer> customers = Arrays.asList(new Customer("1234"), new Customer("5678"));
        ResponseEntity<List<Customer>> resp = new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
        return resp;
    }
    
//    @GetMapping("api/taxi/{plate_number}/requests")
//    public List<Customer> getRequestsMadeToTaxi(@PathVariable("plate_number") String plate_number) {
//        
//        // TODO: Get list of customers who have made requests to taxi with plate_number
//        // Create response payload {"customer": []}
//        List<Customer> customers = Arrays.asList(new Customer("1234"), new Customer("5678"));
//        return customers;
//    }
    
    @PatchMapping("api/customer/{phone_number}/request")
    public boolean acceptOrDenyRequest(@PathVariable("phone_number") String phone_number, boolean accept) {
        
        // TODO: Check if other taxi has already accepted customer.
        // TODO: Update taxi availability.
        return accept;
    }
}
