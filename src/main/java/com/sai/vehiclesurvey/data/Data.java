/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.data;

/**
 * Data object to hold the data details of northBound and southBound.
 *
 * @author sai.dandem
 */
public class Data {

    private long northBoundData;
    private long southBoundData;

    public long getNorthBoundData() {
        return northBoundData;
    }

    public void setNorthBoundData(long northBoundData) {
        this.northBoundData = northBoundData;
    }

    public long getSouthBoundData() {
        return southBoundData;
    }

    public void setSouthBoundData(long southBoundData) {
        this.southBoundData = southBoundData;
    }

    public long getTotalData() {
        return getNorthBoundData() + getSouthBoundData();
    }
}
