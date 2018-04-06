/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

/**
 * Vehicles going in south direction. Recorded on both the sensors.
 *
 * @author sai.dandem
 */
public class SouthBoundVehicle extends Vehicle {

    private Long hoseBFirstRead;

    private Long hoseBSecondRead;

    public SouthBoundVehicle() {
        super();
    }

    public SouthBoundVehicle(long hoseAFirstRead, long hoseBFirstRead, long hoseASecondRead, long hoseBSecondRead,
            double axleGapInMeters) {
        super(hoseAFirstRead, hoseASecondRead, axleGapInMeters);
        if (hoseBFirstRead < 0 || hoseBSecondRead < 0) {
            throw new IllegalArgumentException("Cannot be negative values");
        }
        if (hoseBFirstRead == hoseBSecondRead) {
            throw new IllegalArgumentException("Start read and end read cannot be same values");
        }
        this.hoseBFirstRead = hoseBFirstRead;
        this.hoseBSecondRead = hoseBSecondRead;
    }

    public Long getHoseBFirstRead() {
        return hoseBFirstRead;
    }

    public void setHoseBFirstRead(long hoseBFirstRead) {
        this.hoseBFirstRead = hoseBFirstRead;
    }

    public Long getHoseBSecondRead() {
        return hoseBSecondRead;
    }

    public void setHoseBSecondRead(long hoseBSecondRead) {
        this.hoseBSecondRead = hoseBSecondRead;
    }

    @Override
    public boolean isFull() {
        return super.isFull() && hoseBFirstRead != null && hoseBSecondRead != null;
    }
}
