package com.veoride.taxiservice.model;

import lombok.Data;

@Data
public class CustomerRequest {

    private final Customer customer;
    private final Taxi taxi;
    private CustomerRequestStatus status;

    public CustomerRequest(Customer customer, Taxi taxi) {

        this.customer = customer;
        this.taxi = taxi;
        status = CustomerRequestStatus.PENDING;
    }

    public static enum CustomerRequestStatus {
        PENDING, ACCEPTED, DENIED
    }
}
