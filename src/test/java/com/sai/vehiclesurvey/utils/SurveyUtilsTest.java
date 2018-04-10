/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SurveyUtils utility class.
 *
 * @author sai.dandem
 */
public class SurveyUtilsTest {

    @Test
    public void testConvertToKmsPerHr() {
        assertEquals(18.0, SurveyUtils.convertToKmsPerHr(5.0), 0);
        assertEquals(57.6, SurveyUtils.convertToKmsPerHr(16.0), 0);
        assertEquals(442.8, SurveyUtils.convertToKmsPerHr(123.0), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertToKmsPerHrWithInvalidParam() {
        SurveyUtils.convertToKmsPerHr(-5.0); // Negative values.
    }

    @Test
    public void testCalculateSpeed() {
        assertEquals(2.5, SurveyUtils.calculateSpeed(2.5, 1000, 2000), 0);
        assertEquals(1.0, SurveyUtils.calculateSpeed(3.0, 86399000, 2000), 0); // between 23:59:59:000 to 00:00:02:000  = 3secs gap
        assertEquals(19.5, SurveyUtils.calculateSpeed(2.5, 1000, 1128), 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSpeedWithInvalidTimes() {
        SurveyUtils.calculateSpeed(2.5, 1000, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSpeedWithNegativeParams() {
        SurveyUtils.calculateSpeed(-2.5, 1000, 2000);
    }

    @Test
    public void testConvertMillisToDisplayableHHMM() {
        assertEquals("06:00", SurveyUtils.convertToHHMM(21600000));
        assertEquals("00:30", SurveyUtils.convertToHHMM(1800000));
        assertEquals("00:20", SurveyUtils.convertToHHMM(1200000));
        assertEquals("00:15", SurveyUtils.convertToHHMM(900000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertMillisToDisplayableHHMM_WithNegativeParams() {
        SurveyUtils.convertToHHMM(-2);
    }
    
    @Test
    public void testRound() {
        assertEquals(10.5, SurveyUtils.round(10.4567, 1),0);
        assertEquals(10.46, SurveyUtils.round(10.4567, 2),0);
        assertEquals(10.457, SurveyUtils.round(10.4567, 3),0);
        assertEquals(10.0, SurveyUtils.round(10.4567, 0),0);
        assertEquals(11.0, SurveyUtils.round(10.5567, 0),0);
    }
}
