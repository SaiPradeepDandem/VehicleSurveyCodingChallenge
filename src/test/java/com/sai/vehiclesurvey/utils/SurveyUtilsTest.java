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
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSpeedWithInvalidTimes() {
        SurveyUtils.calculateSpeed(2.5, 1000, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSpeedWithNegativeParams() {
        SurveyUtils.calculateSpeed(-2.5, 1000, 2000);
    }
}
