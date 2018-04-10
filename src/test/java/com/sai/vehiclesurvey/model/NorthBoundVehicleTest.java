/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import com.sai.vehiclesurvey.data.Session;
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
        assertEquals(hoseAFirstRead, nbv.getHoseAFirstRead().longValue());
        assertEquals(hoseASecondRead, nbv.getHoseASecondRead().longValue());
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

    @Test
    public void testIsFull() {
        NorthBoundVehicle nbv = new NorthBoundVehicle(2.5);
        assertEquals(false, nbv.isFull());
        nbv.setHoseAFirstRead(32345);
        assertEquals(false, nbv.isFull());
        nbv.setAxleGapInMeters(2.5);
        assertEquals(false, nbv.isFull());
        nbv.setHoseASecondRead(345567);
        assertEquals(true, nbv.isFull());

    }

    @Test
    public void testFindDistanceInMts_FromFrontVehicle() {
        // Test#1 Front vehicle moving in 60KMPH.
        NorthBoundVehicle nbv1 = new NorthBoundVehicle(1000, 1150, 2.5);
        assertEquals(60, nbv1.getSpeedInKMPH(), 0.001);

        // After 3seconds  second vehicle start crossing the hose.
        NorthBoundVehicle nbv2 = new NorthBoundVehicle(4150, 4300, 2.5);

        // Distance from front vehicle to second vehicle.
        assertEquals(50, nbv2.findDistanceInMts_FromFrontVehicle(nbv1), 0);
        
         // Test#2 Front vehicle moving in 60KMPH.
        NorthBoundVehicle nbv3 = new NorthBoundVehicle(0, 150, 2.5);
        assertEquals(60, nbv3.getSpeedInKMPH(), 0.001);

        // After 3seconds  second vehicle start crossing the hose.
        NorthBoundVehicle nbv4 = new NorthBoundVehicle(600000, 60150, 2.5);

        // Distance from front vehicle to second vehicle.
        assertEquals(9997.5, nbv4.findDistanceInMts_FromFrontVehicle(nbv3), 0.1);
    }
    
    @Test
    public void testIsInSession() {
        NorthBoundVehicle nbv1 = new NorthBoundVehicle(21600000, 21600150, 2.5);
        assertEquals(true, nbv1.isInSession(Session.MORNING));
        assertEquals(false, nbv1.isInSession(Session.EVENING));
        
        NorthBoundVehicle nbv2 = new NorthBoundVehicle(43600000, 43600150, 2.5);
        assertEquals(false, nbv2.isInSession(Session.MORNING));
        assertEquals(true, nbv2.isInSession(Session.EVENING));
    }
    
    @Test
    public void testIsInTimeRange() {
        NorthBoundVehicle nbv1 = new NorthBoundVehicle(21600000, 21600150, 2.5);
        assertEquals(true, nbv1.isInTimeRange(21600000, 21700000));
        assertEquals(false, nbv1.isInTimeRange(22600000, 22700000));
        assertEquals(false, nbv1.isInTimeRange(21600149, 22700000));
    }
}
