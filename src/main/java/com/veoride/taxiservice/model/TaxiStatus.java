package com.veoride.taxiservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaxiStatus {

	private boolean available; // if taxi is available to pick up customer
    private float lat; // current latitude of taxi location
    private float lng; // current longitude of taxi location
}
