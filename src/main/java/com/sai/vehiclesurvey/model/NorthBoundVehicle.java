/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

/**
 * Vehicles going in north direction. Recorded on only one sensor.
 *
 * @author sai.dandem
 */
public class NorthBoundVehicle extends Vehicle {

    public NorthBoundVehicle(double axleGapInMeters) {
        super(axleGapInMeters);
    }

    public NorthBoundVehicle(long hoseAFirstRead, long hoseASecondRead, double axleGapInMeters) {
        super(hoseAFirstRead, hoseASecondRead, axleGapInMeters);
    }

}
