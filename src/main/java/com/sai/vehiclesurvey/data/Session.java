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
    MORNING(21600000, 43200000),
    EVENING(43200000, 64800000);

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

}
