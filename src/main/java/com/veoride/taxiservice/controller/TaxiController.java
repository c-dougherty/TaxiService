package com.veoride.taxiservice.controller;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.CustomerRequest;
import com.veoride.taxiservice.model.Taxi;
import com.veoride.taxiservice.model.CustomerRequest.CustomerRequestStatus;
import com.veoride.taxiservice.service.TaxiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxiController {

    private Map<String, Taxi> taxis;
    private Map<String, CustomerRequest> customerRequests;

    public TaxiController(TaxiService taxiService) {

        taxis = taxiService.getTaxis();
        customerRequests = taxiService.getCustomerRequests();
    }

    /**
     * Update the status of a taxi.
     * 
     * @param plateNumber - plate number of the taxi
     * @param available   - new availability of the taxi
     * @param lat         - new latitude location of the taxi
     * @param lng         - new longitude location of the taxi
     */
    @PatchMapping("api/taxi/{plate_number}")
    public void updateTaxiStatus(@PathVariable("plate_number") String plateNumber, boolean available, float lat,
            float lng) {

        Taxi taxi = taxis.get(plateNumber);
        taxi.setAvailable(available);
        taxi.setLat(lat);
        taxi.setLng(lng);
    }

    /**
     * Get list of customers who have made a request to a specific taxi
     * 
     * @param plateNumber - plate number of specific taxi
     * @return list of customers who have made a request to the taxi
     */
    @GetMapping("api/taxi/{plate_number}/requests")
    public List<Customer> getRequestsMadeToTaxi(@PathVariable("plate_number") String plateNumber) {

        List<Customer> customersWhoRequestedTaxi = new ArrayList<>();

        for (CustomerRequest request : taxis.get(plateNumber).getCustomerRequests().values()) {
            customersWhoRequestedTaxi.add(request.getCustomer());
        }

        return customersWhoRequestedTaxi;
    }

    /**
     * Accept or deny a customer request.
     * 
     * @param phone_number - customer phone number
     * @param accept       - flag indicating whether to accept or deny customer
     *                     request
     * @return true if successfully accepted or denied customer request, false
     *         otherwise.
     */
    @PatchMapping("api/customer/{phone_number}/request")
    public boolean acceptOrDenyRequest(@PathVariable("phone_number") String phone_number, boolean accept) {

        CustomerRequest customerRequest = customerRequests.get(phone_number);

        if (customerRequest == null) {
            return false;
        }

        if (accept) {
            customerRequest.setStatus(CustomerRequestStatus.ACCEPTED);
        }
        else {
            customerRequest.setStatus(CustomerRequestStatus.DENIED);
        }

        String plateNumber = customerRequest.getTaxi().getPlateNumber();
        taxis.get(plateNumber).getCustomerRequests().remove(phone_number);

        return true;
    }
}
