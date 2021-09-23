package com.veoride.taxiservice.controller;

import com.veoride.taxiservice.model.Customer;
import com.veoride.taxiservice.model.CustomerRequest;
import com.veoride.taxiservice.model.Taxi;
import com.veoride.taxiservice.model.TaxiStatus;
import com.veoride.taxiservice.model.CustomerRequest.CustomerRequestStatus;
import com.veoride.taxiservice.service.TaxiService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxiController {

	@Autowired
	TaxiService taxiService;

	/**
	 * Add a new taxi to the taxi service.
	 * 
	 * @param plateNumber - plate number of the taxi
	 * @param status      - status of the taxi
	 */
	@PutMapping("/api/taxi/{plate_number}")
	public void addTaxi(@PathVariable("plate_number") String plateNumber, @RequestBody TaxiStatus status) {

		Taxi taxi = new Taxi(plateNumber, status);
		taxiService.getTaxis().put(plateNumber, taxi);
	}

	/**
	 * Get the info of a specific taxi.
	 * 
	 * @param plateNumber - plate number of the taxi
	 * @return taxi info
	 */
	@GetMapping("/api/taxi/{plate_number}")
	public Taxi getTaxi(@PathVariable("plate_number") String plateNumber) {

		return taxiService.getTaxis().get(plateNumber);
	}

	/**
	 * Update the status of a specific taxi.
	 * 
	 * @param plateNumber - plate number of the taxi
	 * @param status      - new taxi status
	 */
	@PatchMapping("/api/taxi/{plate_number}")
	public void updateTaxiStatus(@PathVariable("plate_number") String plateNumber, @RequestBody TaxiStatus status) {

		Taxi taxi = taxiService.getTaxis().get(plateNumber);
		taxi.setStatus(status);
	}

	/**
	 * Get list of customers who have made a request to a specific taxi.
	 * 
	 * @param plateNumber - plate number of specific taxi
	 * @return list of customers who have made a request to the taxi
	 */
	@GetMapping("/api/taxi/{plate_number}/requests")
	public List<Customer> getRequestsMadeToTaxi(@PathVariable("plate_number") String plateNumber) {

		List<Customer> customersWhoRequestedTaxi = new ArrayList<>();

		for (CustomerRequest request : taxiService.getTaxis().get(plateNumber).getCustomerRequests().values()) {
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
	@PatchMapping("/api/customer/{phone_number}/request")
	public boolean acceptOrDenyRequest(@PathVariable("phone_number") String phone_number, boolean accept) {

		CustomerRequest customerRequest = taxiService.getCustomerRequests().get(phone_number);

		if (customerRequest == null) {
			return false;
		}

		if (accept) {
			customerRequest.setStatus(CustomerRequestStatus.ACCEPTED);
		} else {
			customerRequest.setStatus(CustomerRequestStatus.DENIED);
		}

		String plateNumber = customerRequest.getTaxi().getPlateNumber();
		taxiService.getTaxis().get(plateNumber).getCustomerRequests().remove(phone_number);

		return true;
	}
}
