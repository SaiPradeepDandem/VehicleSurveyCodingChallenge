/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey;

import com.sai.vehiclesurvey.model.EachDayData;
import com.sai.vehiclesurvey.model.NorthBoundVehicle;
import com.sai.vehiclesurvey.model.SouthBoundVehicle;
import com.sai.vehiclesurvey.model.SurveyData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/**
 * Test class for AnalyzeVehicleSurvey.
 *
 * @author sai.dandem
 */
public class AnalyzeVehicleSurveyTest {

    @Test //Passed
    public void testReadDataFile() throws URISyntaxException, IOException {
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        List<String> readingsList = obj.readDataFile();
        assertNotNull(readingsList);
        assertEquals(10, readingsList.size());
    }

    @Test //Passed
    public void testParseOneDayData() {
        List<String> readingsList = Arrays.asList("A268981", "A269123", "A604957", "B604960", "A605128", "B605132",
                "A1089807", "B1089810",
                "A1089948", "B1089951");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayData = surveyData.getDays().get(0);
        assertEquals(1, dayData.getNorthBoundVehicles().size());
        assertEquals(2, dayData.getSouthBoundVehicles().size());

        NorthBoundVehicle nbv = dayData.getNorthBoundVehicles().get(0);
        assertEquals(268981, nbv.getHoseAFirstRead().longValue());
        assertEquals(269123, nbv.getHoseASecondRead().longValue());

        SouthBoundVehicle sbv1 = dayData.getSouthBoundVehicles().get(0);
        assertEquals(604957, sbv1.getHoseAFirstRead().longValue());
        assertEquals(604960, sbv1.getHoseBFirstRead().longValue());
        assertEquals(605128, sbv1.getHoseASecondRead().longValue());
        assertEquals(605132, sbv1.getHoseBSecondRead().longValue());

        SouthBoundVehicle sbv2 = dayData.getSouthBoundVehicles().get(1);
        assertEquals(1089807, sbv2.getHoseAFirstRead().longValue());
        assertEquals(1089810, sbv2.getHoseBFirstRead().longValue());
        assertEquals(1089948, sbv2.getHoseASecondRead().longValue());
        assertEquals(1089951, sbv2.getHoseBSecondRead().longValue());

    }

    @Test // Passed
    public void testParseTwoDayData() {
        List<String> readingsList = Arrays.asList("A86328771", "B86328774", "A86328899", "B86328902", "A582668",
                "B582671", "A582787", "B582789", "A668981", "A669123");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(2, surveyData.getDays().size());

        // Day 1
        final EachDayData dayOneData = surveyData.getDays().get(0);
        assertEquals(0, dayOneData.getNorthBoundVehicles().size());
        assertEquals(1, dayOneData.getSouthBoundVehicles().size());

        SouthBoundVehicle sbv1 = dayOneData.getSouthBoundVehicles().get(0);
        assertEquals(86328771, sbv1.getHoseAFirstRead().longValue());
        assertEquals(86328774, sbv1.getHoseBFirstRead().longValue());
        assertEquals(86328899, sbv1.getHoseASecondRead().longValue());
        assertEquals(86328902, sbv1.getHoseBSecondRead().longValue());

        // Day 2
        final EachDayData dayTwoData = surveyData.getDays().get(1);
        assertEquals(1, dayTwoData.getNorthBoundVehicles().size());
        assertEquals(1, dayTwoData.getSouthBoundVehicles().size());

        NorthBoundVehicle nbv2 = dayTwoData.getNorthBoundVehicles().get(0);
        assertEquals(668981, nbv2.getHoseAFirstRead().longValue());
        assertEquals(669123, nbv2.getHoseASecondRead().longValue());

        SouthBoundVehicle sbv2 = dayTwoData.getSouthBoundVehicles().get(0);
        assertEquals(582668, sbv2.getHoseAFirstRead().longValue());
        assertEquals(582671, sbv2.getHoseBFirstRead().longValue());
        assertEquals(582787, sbv2.getHoseASecondRead().longValue());
        assertEquals(582789, sbv2.getHoseBSecondRead().longValue());
    }

    @Test // Passed
    public void testParseFiveDayData() {
        List<String> readingsList = Arrays.asList("A1000", "B1003", "A1170", "B1173", "A3000", "A3175",
                "A1000", "B1003", "A1170", "B1173", "A3000", "A3175",
                "A1000", "B1003", "A1170", "B1173", "A3000", "A3175",
                "A1000", "B1003", "A1170", "B1173", "A3000", "A3175",
                "A1000", "B1003", "A1170", "B1173", "A3000", "A3175");

        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(5, surveyData.getDays().size());

        for (int i = 0; i < 5; i++) {
            final EachDayData dayData = surveyData.getDays().get(i);
            assertEquals(1, dayData.getNorthBoundVehicles().size());
            assertEquals(1, dayData.getSouthBoundVehicles().size());

            NorthBoundVehicle nbv = dayData.getNorthBoundVehicles().get(0);
            assertEquals(3000, nbv.getHoseAFirstRead().longValue());
            assertEquals(3175, nbv.getHoseASecondRead().longValue());

            SouthBoundVehicle sbv = dayData.getSouthBoundVehicles().get(0);
            assertEquals(1000, sbv.getHoseAFirstRead().longValue());
            assertEquals(1003, sbv.getHoseBFirstRead().longValue());
            assertEquals(1170, sbv.getHoseASecondRead().longValue());
            assertEquals(1173, sbv.getHoseBSecondRead().longValue());
        }
    }

    /**
     * Condition when either lane vehicles have crossed their front axle but not
     * crossed the back axle.
     */
    @Test // Passed
    public void testParseData_When_TwoVehiclesCrossTheHoseAtSimilarTime() {
        List<String> readingsList = Arrays.asList("A86328771", "B86328774", "A86328800", "A86328899", "B86328902",
                "A86328977");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayData = surveyData.getDays().get(0);
        assertEquals(1, dayData.getNorthBoundVehicles().size());
        assertEquals(1, dayData.getSouthBoundVehicles().size());

        NorthBoundVehicle nbv = dayData.getNorthBoundVehicles().get(0);
        assertEquals(86328800, nbv.getHoseAFirstRead().longValue());
        assertEquals(86328977, nbv.getHoseASecondRead().longValue());

        SouthBoundVehicle sbv = dayData.getSouthBoundVehicles().get(0);
        assertEquals(86328771, sbv.getHoseAFirstRead().longValue());
        assertEquals(86328774, sbv.getHoseBFirstRead().longValue());
        assertEquals(86328899, sbv.getHoseASecondRead().longValue());
        assertEquals(86328902, sbv.getHoseBSecondRead().longValue());
    }

    @Test //Passed
    public void testParseData_When_TwoVehiclesCrossesOnSB_WhileOneCrossesOnNB() {
        List<String> readingsList = Arrays.asList("A1000", "A1100", "B1104", "A1270", "B1274", "A2400", "B2404", "A2570",
                "B2574", "A3000");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayData = surveyData.getDays().get(0);
        assertEquals(1, dayData.getNorthBoundVehicles().size());
        assertEquals(2, dayData.getSouthBoundVehicles().size());

        NorthBoundVehicle nbv = dayData.getNorthBoundVehicles().get(0);
        assertEquals(1000, nbv.getHoseAFirstRead().longValue());
        assertEquals(3000, nbv.getHoseASecondRead().longValue());

        SouthBoundVehicle sbv1 = dayData.getSouthBoundVehicles().get(0);
        assertEquals(1100, sbv1.getHoseAFirstRead().longValue());
        assertEquals(1104, sbv1.getHoseBFirstRead().longValue());
        assertEquals(1270, sbv1.getHoseASecondRead().longValue());
        assertEquals(1274, sbv1.getHoseBSecondRead().longValue());

        SouthBoundVehicle sbv2 = dayData.getSouthBoundVehicles().get(1);
        assertEquals(2400, sbv2.getHoseAFirstRead().longValue());
        assertEquals(2404, sbv2.getHoseBFirstRead().longValue());
        assertEquals(2570, sbv2.getHoseASecondRead().longValue());
        assertEquals(2574, sbv2.getHoseBSecondRead().longValue());
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Testing when improper data is provided.
     */
    @Test //Passed
    public void testParseData_When_InvalidReadings() {
        List<String> readingsList = Arrays.asList("A76328771", "A76328772", "B76328774", "A76328899", "A76328901");
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Not a valid data");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);
    }

}
