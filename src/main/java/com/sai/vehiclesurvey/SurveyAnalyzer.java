/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey;

import com.sai.vehiclesurvey.data.Data;
import com.sai.vehiclesurvey.data.Period;
import com.sai.vehiclesurvey.data.Session;
import com.sai.vehiclesurvey.model.EachDayData;
import com.sai.vehiclesurvey.model.SurveyData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for SurveyAnalyzer.
 *
 * @author sai.dandem
 */
public class SurveyAnalyzer {
    private static AtomicInteger lineCnt = new AtomicInteger(0);

    public static void main(String[] args) throws URISyntaxException, IOException {
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        obj.loadAndParseData();
        SurveyData surveyData = obj.getSurveyData();
        System.out.println("No of days data recorded :: " + surveyData.getDays().size());
        analyzeVehicleCount(surveyData);
        System.out.println("");
        analyzePeakVolumeTimes(surveyData);
        System.out.println("");
        analyzeSpeedDistribution(surveyData);
        System.out.println("");
        analyzeDistanceBetweenVehicles(surveyData);

    }

    public static void analyzeVehicleCount(SurveyData surveyData) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("VEHICLE COUNT DATA PER EACH DAY");
        System.out.println("------------------------------------------------------------------");
        Map<Integer, Data<Long>> morningSessionData = surveyData.getVehicleCountBySession(Session.MORNING);
        Map<Integer, Data<Long>> eveningSessionData = surveyData.getVehicleCountBySession(Session.EVENING);
        Map<Integer, Map<String, Data<Long>>> hourData = surveyData.getVehicleCountByPeriod(Period.HOUR);
        Map<Integer, Map<String, Data<Long>>> halfHourData = surveyData.getVehicleCountByPeriod(Period.HALF_HOUR);
        Map<Integer, Map<String, Data<Long>>> data20Min = surveyData.getVehicleCountByPeriod(Period.MIN_20);
        Map<Integer, Map<String, Data<Long>>> qtrHourData = surveyData.getVehicleCountByPeriod(Period.MIN_15);

        for (int i = 0; i < surveyData.getDays().size(); i++) {
            EachDayData dayData = surveyData.getDays().get(i);
            System.out.println("\nDAY NO :: " + (i + 1) + "\n************");
            System.out.println("Total North Bound Vehicles :: " + dayData.getNorthBoundVehicles().size());
            System.out.println("Total South Bound Vehicles :: " + dayData.getSouthBoundVehicles().size());
            System.out.println("");
            System.out.println(
                    "Vehicle count in morninng session (06:00 - 11:59):: " + "NB:" + morningSessionData.get(i + 1).getNorthBoundData() + ", SB:" + morningSessionData.get(
                    i + 1).getSouthBoundData());
            System.out.println(
                    "Vehicle count in evening session (12:00 - 18:00):: " + "NB:" + eveningSessionData.get(i + 1).getNorthBoundData() + ", SB:" + eveningSessionData.get(
                    i + 1).getSouthBoundData());

            Map<String, Data<Long>> hourlyData = hourData.get(i + 1);
            System.out.println("\nVehicle Count Per Hour::");
            displayData(hourlyData);

            Map<String, Data<Long>> halfHourlyData = halfHourData.get(i + 1);
            System.out.println("\nVehicle Count Per Half Hour::");
            displayData(halfHourlyData);

            Map<String, Data<Long>> min20Data = data20Min.get(i + 1);
            System.out.println("\nVehicle Count Per 20 Minutes::");
            displayData(min20Data);

            Map<String, Data<Long>> min15Data = qtrHourData.get(i + 1);
            System.out.println("\nVehicle Count Per 15 Minutes::");
            displayData(min15Data);
        }

        System.out.println("\n------------------------------------------------------------------");
        System.out.println("AVERAGE VEHICLE COUNT DATA FOR ALL DAYS");
        System.out.println("------------------------------------------------------------------");
        Data<Long> avgMorningSessionData = surveyData.getAvgVehicleCountBySession(Session.MORNING);
        Data<Long> avgEveningSessionData = surveyData.getAvgVehicleCountBySession(Session.EVENING);
        Map<String, Data<Long>> avgHourData = surveyData.getAvgVehicleCountByPeriod(Period.HOUR);
        Map<String, Data<Long>> avgHalfHourData = surveyData.getAvgVehicleCountByPeriod(Period.HALF_HOUR);
        Map<String, Data<Long>> avgData20Min = surveyData.getAvgVehicleCountByPeriod(Period.MIN_20);
        Map<String, Data<Long>> avgQtrHourData = surveyData.getAvgVehicleCountByPeriod(Period.MIN_15);

        System.out.println(
                "Average vehicle count in morninng session (06:00 - 11:59):: " + "NB:" + avgMorningSessionData.getNorthBoundData() + ", SB:" + avgMorningSessionData.getSouthBoundData());
        System.out.println(
                "Average vehicle count in evening session (12:00 - 18:00):: " + "NB:" + avgEveningSessionData.getNorthBoundData() + ", SB:" + avgEveningSessionData.getSouthBoundData());
        System.out.println("");

        System.out.println("\nAverage Vehicle Count Per Hour::");
        displayData(avgHourData);

        System.out.println("\nAverage Vehicle Count Per Half Hour::");
        displayData(avgHalfHourData);

        System.out.println("\nAverage Vehicle Count Per 20 Minutes::");
        displayData(avgData20Min);

        System.out.println("\nAverage Vehicle Count Per 15 Minutes::");
        displayData(avgQtrHourData);

    }

    public static void analyzePeakVolumeTimes(SurveyData surveyData) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("PEAK VOLUME TIMES PER EACH DAY");
        System.out.println("------------------------------------------------------------------");
        Map<Integer, Data<String>> morningSessionData = surveyData.getPeakVolumeTimeBySession(Session.MORNING);
        Map<Integer, Data<String>> eveningSessionData = surveyData.getPeakVolumeTimeBySession(Session.EVENING);

        for (int i = 0; i < surveyData.getDays().size(); i++) {
            System.out.println("\nDAY NO :: " + (i + 1) + "\n************");
            System.out.println(
                    "Peak volume time in morninng session (06:00 - 11:59):: " + "NB:" + morningSessionData.get(i + 1).getNorthBoundData() + ", SB:" + morningSessionData.get(
                    i + 1).getSouthBoundData());
            System.out.println(
                    "Peak volume time in evening session (12:00 - 18:00):: " + "NB:" + eveningSessionData.get(i + 1).getNorthBoundData() + ", SB:" + eveningSessionData.get(
                    i + 1).getSouthBoundData());
        }
    }

    public static void analyzeSpeedDistribution(SurveyData surveyData) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("VEHICLE SPEED DATA PER EACH DAY");
        System.out.println("------------------------------------------------------------------");
        Map<Integer, Data<Double>> morningSessionData = surveyData.getVehicleSpeedBySession(Session.MORNING);
        Map<Integer, Data<Double>> eveningSessionData = surveyData.getVehicleSpeedBySession(Session.EVENING);
        Map<Integer, Map<String, Data<Double>>> hourData = surveyData.getVehicleSpeedByPeriod(Period.HOUR);
        Map<Integer, Map<String, Data<Double>>> halfHourData = surveyData.getVehicleSpeedByPeriod(Period.HALF_HOUR);
        Map<Integer, Map<String, Data<Double>>> data20Min = surveyData.getVehicleSpeedByPeriod(Period.MIN_20);
        Map<Integer, Map<String, Data<Double>>> qtrHourData = surveyData.getVehicleSpeedByPeriod(Period.MIN_15);

        for (int i = 0; i < surveyData.getDays().size(); i++) {
            EachDayData dayData = surveyData.getDays().get(i);
            System.out.println("\nDAY NO :: " + (i + 1) + "\n************");
            System.out.println(
                    "Avg Vehicle speed in morninng session (06:00 - 11:59):: " + "NB:" + morningSessionData.get(i + 1).getNorthBoundData() + ", SB:" + morningSessionData.get(
                    i + 1).getSouthBoundData());
            System.out.println(
                    "Avg Vehicle speed in evening session (12:00 - 18:00):: " + "NB:" + eveningSessionData.get(i + 1).getNorthBoundData() + ", SB:" + eveningSessionData.get(
                    i + 1).getSouthBoundData());

            Map<String, Data<Double>> hourlyData = hourData.get(i + 1);
            System.out.println("\nVehicle Speed Per Hour::");
            displayData(hourlyData);

            Map<String, Data<Double>> halfHourlyData = halfHourData.get(i + 1);
            System.out.println("\nVehicle Speed Per Half Hour::");
            displayData(halfHourlyData);

            Map<String, Data<Double>> min20Data = data20Min.get(i + 1);
            System.out.println("\nVehicle Speed Per 20 Minutes::");
            displayData(min20Data);

            Map<String, Data<Double>> min15Data = qtrHourData.get(i + 1);
            System.out.println("\nVehicle Speed Per 15 Minutes::");
            displayData(min15Data);
        }

        System.out.println("\n------------------------------------------------------------------");
        System.out.println("AVERAGE VEHICLE SPEED DATA FOR ALL DAYS");
        System.out.println("------------------------------------------------------------------");
        Data<Double> avgMorningSessionData = surveyData.getAvgVehicleSpeedBySession(Session.MORNING);
        Data<Double> avgEveningSessionData = surveyData.getAvgVehicleSpeedBySession(Session.EVENING);
        Map<String, Data<Double>> avgHourData = surveyData.getAvgVehicleSpeedByPeriod(Period.HOUR);
        Map<String, Data<Double>> avgHalfHourData = surveyData.getAvgVehicleSpeedByPeriod(Period.HALF_HOUR);
        Map<String, Data<Double>> avgData20Min = surveyData.getAvgVehicleSpeedByPeriod(Period.MIN_20);
        Map<String, Data<Double>> avgQtrHourData = surveyData.getAvgVehicleSpeedByPeriod(Period.MIN_15);

        System.out.println(
                "Average vehicle speed in morninng session (06:00 - 11:59):: " + "NB:" + avgMorningSessionData.getNorthBoundData() + ", SB:" + avgMorningSessionData.getSouthBoundData());
        System.out.println(
                "Average vehicle speed in evening session (12:00 - 18:00):: " + "NB:" + avgEveningSessionData.getNorthBoundData() + ", SB:" + avgEveningSessionData.getSouthBoundData());
        System.out.println("");

        System.out.println("\nAverage Vehicle Speed Per Hour::");
        displayData(avgHourData);

        System.out.println("\nAverage Vehicle Speed Per Half Hour::");
        displayData(avgHalfHourData);

        System.out.println("\nAverage Vehicle Speed Per 20 Minutes::");
        displayData(avgData20Min);

        System.out.println("\nAverage Vehicle Speed Per 15 Minutes::");
        displayData(avgQtrHourData);

    }

    public static void analyzeDistanceBetweenVehicles(SurveyData surveyData) {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("AVERAGE DISTANCE BETWEEN VEHICLE DATA FOR ALL DAYS");
        System.out.println("------------------------------------------------------------------");
        Map<String, Data<Double>> avgHourData = surveyData.getAvgDistanceBetweenVehicles_ByPeriod(Period.HOUR);
        Map<String, Data<Double>> avgHalfHourData = surveyData.getAvgDistanceBetweenVehicles_ByPeriod(Period.HALF_HOUR);
        Map<String, Data<Double>> avgData20Min = surveyData.getAvgDistanceBetweenVehicles_ByPeriod(Period.MIN_20);
        Map<String, Data<Double>> avgQtrHourData = surveyData.getAvgDistanceBetweenVehicles_ByPeriod(Period.MIN_15);

        System.out.println("\nAverage distance between vehicles Per Hour (in meters)::");
        displayData(avgHourData);

        System.out.println("\nAverage distance between vehicles Per Half Hour (in meters)::");
        displayData(avgHalfHourData);

        System.out.println("\nAverage distance between vehicles Per 20 Minutes (in meters)::");
        displayData(avgData20Min);

        System.out.println("\nAverage distance between vehicles Per 15 Minutes (in meters)::");
        displayData(avgQtrHourData);

    }

    private static <T, M> void displayData(Map<T, Data<M>> dataMap) {
        lineCnt.set(0);
        dataMap.forEach((p, data) -> {
            System.out.print(p + " - NB:" + data.getNorthBoundData() + ", SB:" + data.getSouthBoundData() + "");
            if (lineCnt.getAndIncrement() % 4 == 0 && lineCnt.get() - 1 != 0) {
                System.out.println("");
            } else {
                System.out.print(";  ");
            }
        });
        System.out.println("");
    }
}
