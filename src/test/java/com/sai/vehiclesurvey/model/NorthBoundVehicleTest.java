/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NorthBoundVehicle model object.
 *
 * @author sai.dandem
 */
public class NorthBoundVehicleTest {

    @Test
    public void initiateNorthBoundVehicleWithValidParams() {
        final long hoseAFirstRead = 47859;
        final long hoseASecondRead = 48956;
        final double axleGapInMeters = 2.5;
        NorthBoundVehicle nbv = new NorthBoundVehicle(hoseAFirstRead, hoseASecondRead, axleGapInMeters);
        assertEquals(hoseAFirstRead, nbv.getHoseAFirstRead());
        assertEquals(hoseASecondRead, nbv.getHoseASecondRead());
        assertEquals(axleGapInMeters, nbv.getAxleGapInMeters(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initiateNorthBoundVehicleWithInvalidHoseReadings() {
        new NorthBoundVehicle(-1000, 2000, 2.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initiateNorthBoundVehicleWithInvalidAxleGap() {
        new NorthBoundVehicle(47859, 48956, 0);
    }

    @Test
    public void testGetSpeed() {
        NorthBoundVehicle nbv = new NorthBoundVehicle(1000, 4000, 3);
        assertEquals(1.0, nbv.getSpeedInMPS(), 0);
        assertEquals(3.6, nbv.getSpeedInKMPH(), 0);
    }

}
