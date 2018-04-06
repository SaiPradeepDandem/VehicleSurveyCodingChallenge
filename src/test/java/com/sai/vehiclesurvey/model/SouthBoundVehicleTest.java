/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SouthBoundVehicle model object.
 *
 * @author sai.dandem
 */
public class SouthBoundVehicleTest {

    @Test
    public void initiateSouthBoundVehicleWithValidParams() {
        final long hoseAFirstRead = 47859;
        final long hoseASecondRead = 48956;
        final long hoseBFirstRead = 47863;
        final long hoseBSecondRead = 48960;
        final double axleGapInMeters = 2.5;
        SouthBoundVehicle sbv = new SouthBoundVehicle(hoseAFirstRead, hoseBFirstRead, hoseASecondRead, hoseBSecondRead,
                axleGapInMeters);
        assertEquals(hoseAFirstRead, sbv.getHoseAFirstRead().longValue());
        assertEquals(hoseASecondRead, sbv.getHoseASecondRead().longValue());
        assertEquals(axleGapInMeters, sbv.getAxleGapInMeters(), 0);
        assertEquals(hoseBFirstRead, sbv.getHoseBFirstRead().longValue());
        assertEquals(hoseBSecondRead, sbv.getHoseBSecondRead().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void initiateSouthBoundVehicleWithInvalidHoseReadings() {
        new SouthBoundVehicle(1000, 1004, 2000, -2004, 2.5);
    }

    @Test
    public void testIsFull() {
        SouthBoundVehicle sbv = new SouthBoundVehicle();
        assertEquals(false, sbv.isFull());
        sbv.setHoseAFirstRead(32345);
        assertEquals(false, sbv.isFull());
        sbv.setAxleGapInMeters(2.5);
        assertEquals(false, sbv.isFull());
        sbv.setHoseASecondRead(345567);
        assertEquals(false, sbv.isFull());
        sbv.setHoseBFirstRead(32348);
        assertEquals(false, sbv.isFull());
        sbv.setHoseBSecondRead(345569);
        assertEquals(true, sbv.isFull());
    }
}
