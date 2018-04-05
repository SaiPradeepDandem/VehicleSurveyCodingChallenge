/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey;

import com.sai.vehiclesurvey.model.EachDayData;
import com.sai.vehiclesurvey.model.SurveyData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for AnalyzeVehicleSurvey.
 *
 * @author sai.dandem
 */
public class AnalyzeVehicleSurveyTest {

    private List<String> getSampleOneDayData() {
        return Arrays.asList("A268981", "A269123", "A604957", "B604960", "A605128", "B605132", "A1089807", "B1089810",
                "A1089948", "B1089951");
    }

    private List<String> getSampleTwoDayData() {
        return Arrays.asList("A86328771", "B86328774", "A86328899", "B86328902", "A582668", "B582671", "A582787",
                "B582789", "A668981", "A669123");
    }

    @Test
    public void testReadDataFile() throws URISyntaxException, IOException {
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        List<String> readingsList = obj.readDataFile();
        assertNotNull(readingsList);
        assertEquals(10, readingsList.size());
    }

    @Test
    public void testParseOneDayData() {
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        List<String> readingsList = getSampleOneDayData();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayData = surveyData.getDays().get(0);
        assertEquals(1, dayData.getNorthBoundVehicles().size());
        assertEquals(2, dayData.getSouthBoundVehicles().size());
    }

    @Test
    public void testParseTwoDayData() {
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        List<String> readingsList = getSampleTwoDayData();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(2, surveyData.getDays().size());

        final EachDayData dayOneData = surveyData.getDays().get(0);
        assertEquals(1, dayOneData.getNorthBoundVehicles().size());
        assertEquals(1, dayOneData.getSouthBoundVehicles().size());

        final EachDayData dayTwoData = surveyData.getDays().get(0);
        assertEquals(1, dayTwoData.getNorthBoundVehicles().size());
        assertEquals(1, dayTwoData.getSouthBoundVehicles().size());
    }

    /**
     * Condition when either lane vehicles have crossed their front axle but not crossed the back axle.
     */
    @Test
    public void testParseData_When_TwoVehiclesCrossTheHoseAtSimilarTime() {
        List<String> readingsList = Arrays.asList("A86328771", "B86328774", "A86328800", "A86328899", "B86328902",
                "A86328977");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayOneData = surveyData.getDays().get(0);
        assertEquals(1, dayOneData.getNorthBoundVehicles().size());
        assertEquals(1, dayOneData.getSouthBoundVehicles().size());
    }

    /**
     * Condition when a vehicle on one lane start crossing before the other lane vehicle's back axle crosses
     * Hose-B.(back axle already crossed Hose-A)
     */
    @Test
    public void testParseData_When_AVehicleStarts_BeforeOtherVehiclesBack_FinishesB() {
        List<String> readingsList = Arrays.asList("A76328771", "B76328774", "A76328899", "A76328900", "B76328902",
                "A76329071");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayOneData = surveyData.getDays().get(0);
        assertEquals(1, dayOneData.getNorthBoundVehicles().size());
        assertEquals(1, dayOneData.getSouthBoundVehicles().size());
    }

    /**
     * VERY RARE CONDITION
     * Condition when a vehicle(V1) on one lane start crossing before the other lane vehicle's(V2) front axle crosses
     * Hose-B(front axle already crossed Hose-A). And V1 back axle crosses Hose-A before V2 back axle crosses Hose-B
     * (back axle already crossed Hose-A).
     */
    @Test
    public void testParseData_When_AVehicleStarts_BeforeOtherVehiclesFront_FinishesB() {
        List<String> readingsList = Arrays.asList("A76328771", "A76328772", "B76328774", "A76328899", "A76328901",
                "B76328902");
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.parseData(readingsList);

        final SurveyData surveyData = obj.getSurveyData();
        assertNotNull(surveyData);
        assertEquals(1, surveyData.getDays().size());

        final EachDayData dayOneData = surveyData.getDays().get(0);
        assertEquals(1, dayOneData.getNorthBoundVehicles().size());
        assertEquals(1, dayOneData.getSouthBoundVehicles().size());
    }

}
