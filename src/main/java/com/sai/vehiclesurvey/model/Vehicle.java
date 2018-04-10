/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import com.sai.vehiclesurvey.data.Session;
import com.sai.vehiclesurvey.utils.SurveyUtils;

/**
 * Base class for Vehicle.
 *
 * @author sai.dandem
 */
public abstract class Vehicle {

    private Long hoseAFirstRead;

    private Long hoseASecondRead;

    private double axleGapInMeters;

    public Vehicle(double axleGapInMeters) {
        if (axleGapInMeters < 0) {
            throw new IllegalArgumentException("Cannot be negative values");
        }
        this.axleGapInMeters = axleGapInMeters;
    }

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

    public Long getHoseAFirstRead() {
        return hoseAFirstRead;
    }

    public void setHoseAFirstRead(long hoseAFirstRead) {
        this.hoseAFirstRead = hoseAFirstRead;
    }

    public Long getHoseASecondRead() {
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

    /**
     * Calculates the distance from the front vehicle.
     *
     * @param frontVehicle Front vehicle.
     * @return Distance in meters.
     */
    public double findDistanceInMts_FromFrontVehicle(Vehicle frontVehicle) {
        double timeDiffInSecs = (double)(this.hoseAFirstRead - frontVehicle.hoseASecondRead) / 1000;
        double speedInMts = frontVehicle.getSpeedInMPS();
        return speedInMts * timeDiffInSecs;
    }

    /**
     * Determines whether the vehicle is in the provided session time range.
     *
     * @param session Session time.
     * @return {@code true} if the vehicle is in the range else returns {@code false}.
     */
    public boolean isInSession(Session session) {
        return isInTimeRange(session.getStart(), session.getEnd());
    }

    /**
     * Determines whether the vehicle is in the provided time range.
     *
     * @param start Start time of the range.
     * @param end   End time of the range.
     * @return {@code true} if the vehicle is in the range else returns {@code false}.
     */
    public boolean isInTimeRange(long start, long end) {
        return hoseAFirstRead >= start && hoseAFirstRead <= end;
    }

    /**
     * Utility method to check whether both the readings of the hose are
     * recorded or not.
     *
     * @return {@code true} if both the readings are present, else returns
     *         {@code false}.
     */
    public boolean isFull() {
        return hoseAFirstRead != null && hoseASecondRead != null;
    }
}
