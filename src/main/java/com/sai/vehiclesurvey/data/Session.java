/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.data;

/**
 * Day session time range enum.
 *
 * @author sai.dandem
 */
public enum Session {
    MORNING(21600000, 43199999),
    EVENING(43200000, 64799999);

    private final long start;

    private final long end;

    private Session(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public boolean containsPeriod(long periodStart, long periodEnd) {
        return (periodStart >= this.start && periodStart < this.end) && (periodEnd > this.start && periodEnd <= this.end);
    }
}
