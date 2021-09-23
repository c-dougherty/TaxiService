package com.veoride.taxiservice.controller;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.CustomerRequest;
import com.veoride.taxiservice.model.Taxi;
import com.veoride.taxiservice.model.CustomerRequest.CustomerRequestStatus;
import com.veoride.taxiservice.service.TaxiService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private static final Long REQUEST_TIMEOUT = 60000L;
    private static final Long WAIT_INTERVAL = 5000L;
    
    @Autowired
    TaxiService taxiService;

    /**
     * Get a list of nearby taxis.
     * 
     * @param distance - max distance from location to taxi
     * @param lat      - latitude of current location
     * @param lng      - longitude of current location
     * @return list of taxis within distance from the current location.
     */
    @GetMapping("/api/taxi/nearby")
    public List<Taxi> getNearbyTaxis(@RequestParam(name = "distance") float distance,
            @RequestParam(name = "lat") float lat, @RequestParam(name = "lng") float lng) {

        return taxiService.getTaxis().values().stream().filter(taxi -> distanceFromTaxi(lat, lng, taxi) < distance).toList();
    }

    /**
     * Send a request to each taxi in a list of taxis one at a time until a taxi
     * accepts a request, or there are no more taxis in the list to request, or a
     * timeout (1 minute) is reached.
     * 
     * @param phoneNumber  - customer phone number
     * @param plateNumbers - list of taxi plate numbers
     * @return the plate number of the taxi that accepted the request, null
     *         otherwise.
     * @throws InterruptedException
     */
    @PostMapping("/api/customer/{phone_number}/request")
    public String sendRequestToTaxis(@PathVariable("phone_number") String phoneNumber, List<String> plateNumbers)
            throws InterruptedException {

        Customer customer = taxiService.getCustomers().get(phoneNumber);
        long waitTime = 0L;

        for (String plateNumber : plateNumbers) {

            Taxi taxi = taxiService.getTaxis().get(plateNumber);

            if (taxi.getStatus().isAvailable()) {
                CustomerRequest customerRequest = new CustomerRequest(customer, taxi);
                taxi.getCustomerRequests().put(phoneNumber, customerRequest);
                taxiService.getCustomerRequests().put(phoneNumber, customerRequest);

                while (customerRequest.getStatus().equals(CustomerRequestStatus.PENDING)) {
                    Thread.sleep(WAIT_INTERVAL);
                    waitTime += WAIT_INTERVAL;

                    if (waitTime >= REQUEST_TIMEOUT) {
                        taxiService.getCustomerRequests().remove(phoneNumber);
                        taxi.getCustomerRequests().remove(phoneNumber);
                        return null;
                    }
                }

                if (customerRequest.getStatus().equals(CustomerRequestStatus.ACCEPTED)) {
                    taxiService.getCustomerRequests().remove(phoneNumber);
                    taxi.getCustomerRequests().remove(phoneNumber);
                    return taxi.getPlateNumber();
                }
            }
        }

        taxiService.getCustomerRequests().remove(phoneNumber);
        return null;
    }

    /**
     * Return the distance between the latitude, longitude location and the taxi
     * location.
     * 
     * @param lat  - latitude location
     * @param lng  - longitude location
     * @param taxi - taxi
     * @return the distance between the provided location and the taxi.
     */
    private float distanceFromTaxi(float lat, float lng, Taxi taxi) {

        float latDistance = Math.abs(lat - taxi.getStatus().getLat());
        float lngDistance = Math.abs(lng - taxi.getStatus().getLng());

        return (float) Math.hypot(latDistance, lngDistance);
    }

}
