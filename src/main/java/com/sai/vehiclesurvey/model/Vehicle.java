/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import com.sai.vehiclesurvey.utils.SurveyUtils;

/**
 * Base class for Vehicle.
 *
 * @author sai.dandem
 */
public abstract class Vehicle {

    private long hoseAFirstRead;

    private long hoseASecondRead;

    private double axleGapInMeters;

    public Vehicle(long hoseAFirstRead, long hoseASecondRead, double axleGapInMeters) {
        if (hoseAFirstRead < 0 || hoseASecondRead < 0 || axleGapInMeters < 0) {
            throw new IllegalArgumentException("Cannot be negative values");
        }
        if (hoseAFirstRead == hoseASecondRead) {
            throw new IllegalArgumentException("Start read and end read cannot be same values");
        }
        if (axleGapInMeters == 0) {
            throw new IllegalArgumentException("Axle gap cannot be 0");
        }
        this.hoseAFirstRead = hoseAFirstRead;
        this.hoseASecondRead = hoseASecondRead;
        this.axleGapInMeters = axleGapInMeters;
    }

    public long getHoseAFirstRead() {
        return hoseAFirstRead;
    }

    public void setHoseAFirstRead(long hoseAFirstRead) {
        this.hoseAFirstRead = hoseAFirstRead;
    }

    public long getHoseASecondRead() {
        return hoseASecondRead;
    }

    public void setHoseASecondRead(long hoseASecondRead) {
        this.hoseASecondRead = hoseASecondRead;
    }

    public double getAxleGapInMeters() {
        return axleGapInMeters;
    }

    public void setAxleGapInMeters(double axleGapInMeters) {
        this.axleGapInMeters = axleGapInMeters;
    }

    /**
     * Returns the speed of the vehicle in meters per second.
     *
     * @return Speed in m/s.
     */
    public final double getSpeedInMPS() {
        return SurveyUtils.calculateSpeed(axleGapInMeters, hoseAFirstRead, hoseASecondRead);
    }

    /**
     * Returns the speed of the vehicle in kms per hour.
     *
     * @return Speed in km/h.
     */
    public final double getSpeedInKMPH() {
        return SurveyUtils.convertToKmsPerHr(getSpeedInMPS());
    }
}
