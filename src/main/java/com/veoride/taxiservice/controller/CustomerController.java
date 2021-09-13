package com.veoride.taxiservice.controller;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.CustomerRequest;
import com.veoride.taxiservice.model.Taxi;
import com.veoride.taxiservice.model.CustomerRequest.CustomerRequestStatus;
import com.veoride.taxiservice.service.TaxiService;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private static final Long REQUEST_TIMEOUT = 60000L;
    private static final Long WAIT_INTERVAL = 5000L;

    private Map<String, Taxi> taxis;
    private Map<String, Customer> customers;
    private Map<String, CustomerRequest> customerRequests;

    public CustomerController(TaxiService taxiService) {

        taxis = taxiService.getTaxis();
        customers = taxiService.getCustomers();
        customerRequests = taxiService.getCustomerRequests();
    }

    /**
     * Get a list of nearby taxis.
     * 
     * @param distance - max distance from location to taxi
     * @param lat      - latitude of current location
     * @param lng      - longitude of current location
     * @return list of taxis within distance from the current location.
     */
    @GetMapping("api/taxi/nearby")
    public List<Taxi> getNearbyTaxis(@RequestParam(name = "distance") float distance,
            @RequestParam(name = "lat") float lat, @RequestParam(name = "lng") float lng) {

        return taxis.values().stream().filter(taxi -> distanceFromTaxi(lat, lng, taxi) < distance).toList();
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
    @PostMapping("api/customer/{phone_number}/request")
    public String sendRequestToTaxis(@PathVariable("phone_number") String phoneNumber, List<String> plateNumbers)
            throws InterruptedException {

        Customer customer = customers.get(phoneNumber);
        long waitTime = 0L;

        for (String plateNumber : plateNumbers) {

            Taxi taxi = taxis.get(plateNumber);

            if (taxi.isAvailable()) {
                CustomerRequest customerRequest = new CustomerRequest(customer, taxi);
                taxi.getCustomerRequests().put(phoneNumber, customerRequest);
                customerRequests.put(phoneNumber, customerRequest);

                while (customerRequest.getStatus().equals(CustomerRequestStatus.PENDING)) {
                    Thread.sleep(WAIT_INTERVAL);
                    waitTime += WAIT_INTERVAL;

                    if (waitTime >= REQUEST_TIMEOUT) {
                        customerRequests.remove(phoneNumber);
                        taxi.getCustomerRequests().remove(phoneNumber);
                        return null;
                    }
                }

                if (customerRequest.getStatus().equals(CustomerRequestStatus.ACCEPTED)) {
                    customerRequests.remove(phoneNumber);
                    taxi.getCustomerRequests().remove(phoneNumber);
                    return taxi.getPlateNumber();
                }
            }
        }

        customerRequests.remove(phoneNumber);
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

        float latDistance = Math.abs(lat - taxi.getLat());
        float lngDistance = Math.abs(lng - taxi.getLng());

        return (float) Math.hypot(latDistance, lngDistance);
    }

}
