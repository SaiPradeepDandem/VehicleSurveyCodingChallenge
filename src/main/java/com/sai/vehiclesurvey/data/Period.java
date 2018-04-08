/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.data;

/**
 * Enum values for time periods.
 *
 * @author sai.dandem
 */
public enum Period {
    HOUR(3600000),
    HALF_HOUR(1800000),
    MIN_20(1200000),
    MIN_15(900000);

    private final long period;

    private Period(long period) {
        this.period = period;
    }

    public long getPeriod() {
        return period;
    }

}
