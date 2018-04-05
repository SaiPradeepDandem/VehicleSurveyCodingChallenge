/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.utils;

/**
 * Utility class for vehicle survey calculations.
 *
 * @author sai.dandem
 */
public class SurveyUtils {
    public static final long MILLIS_IN_24HRS = 86_400_000;

    /**
     * Converts the given meters per second speed to kilometers per hour value.
     *
     * @param mtsPerSec Speed in mps.
     * @return Speed in kmph
     */
    public static double convertToKmsPerHr(double mtsPerSec) {
        if (mtsPerSec < 0) {
            throw new IllegalArgumentException("Cannot be negative values");
        }
        return mtsPerSec * 3.6; // 1 m/s = 3.6 km/h
    }

    /**
     * Calculates the speed in meter per sec (mps) for the provided distance and time period.
     *
     * @param distanceInMts Distance traveled in meters.
     * @param startTimeInMs Start time in milliseconds
     * @param endTimeInMs   End time in milli seconds.
     * @return Speed in m/s.
     */
    public static double calculateSpeed(double distanceInMts, long startTimeInMs, long endTimeInMs) {
        if (distanceInMts < 0 || startTimeInMs < 0 || endTimeInMs < 0) {
            throw new IllegalArgumentException("Cannot be negative values");
        }
        if (startTimeInMs == endTimeInMs) {
            throw new IllegalArgumentException("Start time and end time cannot be same values");
        }
        if (endTimeInMs < startTimeInMs) {
            endTimeInMs = startTimeInMs + (MILLIS_IN_24HRS - startTimeInMs) + endTimeInMs;
        }
        double diffInSecs = (endTimeInMs - startTimeInMs) / 1000;
        return distanceInMts / diffInSecs;
    }
}
