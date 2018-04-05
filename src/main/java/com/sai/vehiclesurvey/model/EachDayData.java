/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object to hold the data of vehicles crossed in 24Hrs of each day.
 *
 * @author sai.dandem
 */
public class EachDayData {

    private final List<NorthBoundVehicle> northBoundVehicles = new ArrayList<>();

    private final List<SouthBoundVehicle> southBoundVehicles = new ArrayList<>();

    public List<NorthBoundVehicle> getNorthBoundVehicles() {
        return northBoundVehicles;
    }

    public List<SouthBoundVehicle> getSouthBoundVehicles() {
        return southBoundVehicles;
    }

}
