package com.veoride.taxiservice.model;

import lombok.Data;

@Data
public class Customer {

    private final String phone;     // customer phone number
    
    private float lat;              // current latitude of customer location
    private float lng;              // current longitude of customer location
    
    public Customer(String phone) {
        this.phone = phone;
        lat = 0;
        lng = 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer{customer=");
        sb.append(this);
        sb.append('}');
        return  sb.toString();
    }
}
