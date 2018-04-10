/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import com.sai.vehiclesurvey.data.Data;
import com.sai.vehiclesurvey.data.Period;
import com.sai.vehiclesurvey.data.Session;
import com.sai.vehiclesurvey.utils.SurveyUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for EachDayData model object.
 *
 * @author sai.dandem
 */
public class EachDayDataTest {
    @Test
    public void testGetVehicleCountBySession() {
        EachDayData eachDayData = generateDataBy10Mins();
        Data<Long> morningSessionData = new Data(36L, 36L);
        Data<Long> eveningSessionData = new Data(36L, 36L);
        assertEquals(morningSessionData, eachDayData.getVehicleCountBySession(Session.MORNING));
        assertEquals(eveningSessionData, eachDayData.getVehicleCountBySession(Session.EVENING));

        // No Valid data
        List<NorthBoundVehicle> nbvs = new ArrayList<>();
        nbvs.add(new NorthBoundVehicle(71600000, 71600150, 2.5));
        nbvs.add(new NorthBoundVehicle(71700000, 71700150, 2.5));

        List<SouthBoundVehicle> sbvs = new ArrayList<>();
        sbvs.add(new SouthBoundVehicle(65610000, 65610003, 65610150, 65610153, 2.5));

        EachDayData edd = new EachDayData();
        edd.getNorthBoundVehicles().addAll(nbvs);
        edd.getSouthBoundVehicles().addAll(sbvs);

        Data<Long> msd = new Data(0L, 0L);
        Data<Long> esd = new Data(0L, 0L);
        assertEquals(msd, edd.getVehicleCountBySession(Session.MORNING));
        assertEquals(esd, edd.getVehicleCountBySession(Session.EVENING));
    }

    @Test
    public void testGetVehicleCountByPeriod() {
        EachDayData eachDayData = generateDataBy10Mins();

        AtomicLong inc = new AtomicLong();
        Map<String, Data<Long>> perHourData = eachDayData.getVehicleCountByPeriod(Period.HOUR);
        assertEquals(24, perHourData.size());
        inc.set(0);
        long millisInHr = Period.HOUR.getPeriod();
        perHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInHr)));
            assertEquals(6L, data.getNorthBoundData().longValue());
            assertEquals(6L, data.getSouthBoundData().longValue());
        });

        Map<String, Data<Long>> perHalfHourData = eachDayData.getVehicleCountByPeriod(Period.HALF_HOUR);
        assertEquals(48, perHalfHourData.size());
        inc.set(0);
        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        perHalfHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInHalfHr)));
            assertEquals(3L, data.getNorthBoundData().longValue());
            assertEquals(3L, data.getSouthBoundData().longValue());
        });

        Map<String, Data<Long>> per20MinsData = eachDayData.getVehicleCountByPeriod(Period.MIN_20);
        assertEquals(72, per20MinsData.size());
        inc.set(0);
        long millisIn20Mins = Period.MIN_20.getPeriod();
        per20MinsData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisIn20Mins)));
            assertEquals(2L, data.getNorthBoundData().longValue());
            assertEquals(2L, data.getSouthBoundData().longValue());
        });

        Map<String, Data<Long>> perQuarterHourData = eachDayData.getVehicleCountByPeriod(Period.MIN_15);
        assertEquals(96, perQuarterHourData.size());
        inc.set(0);
        long millisInQtrHr = Period.MIN_15.getPeriod();
        AtomicInteger index = new AtomicInteger(0);
        perQuarterHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInQtrHr)));
            if (index.getAndIncrement() % 2 == 0) {
                assertEquals(2L, data.getNorthBoundData().longValue());
                assertEquals(2L, data.getSouthBoundData().longValue());
            } else {
                assertEquals(1L, data.getNorthBoundData().longValue());
                assertEquals(1L, data.getSouthBoundData().longValue());
            }
        });

    }

    @Test
    public void testVehicleSpeedBySession() {
        EachDayData eachDayData = generateDataBy10Mins();
        Data<Double> morningSessionData = new Data(60.0, 60.0);
        Data<Double> eveningSessionData = new Data(60.0, 60.0);
        assertEquals(morningSessionData, eachDayData.getVehicleSpeedBySession(Session.MORNING));
        assertEquals(eveningSessionData, eachDayData.getVehicleSpeedBySession(Session.EVENING));
    }

    @Test
    public void testVehicleSpeedByPeriod() {
        EachDayData eachDayData = generateDataBy10Mins();

        AtomicLong inc = new AtomicLong();
        Map<String, Data<Double>> perHourData = eachDayData.getVehicleSpeedByPeriod(Period.HOUR);
        assertEquals(24, perHourData.size());
        inc.set(0);
        long millisInHr = Period.HOUR.getPeriod();
        perHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInHr)));
            assertEquals(60.0, data.getNorthBoundData(), 0);
            assertEquals(60.0, data.getSouthBoundData(), 0);
        });

        Map<String, Data<Double>> perHalfHourData = eachDayData.getVehicleSpeedByPeriod(Period.HALF_HOUR);
        assertEquals(48, perHalfHourData.size());
        inc.set(0);
        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        perHalfHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInHalfHr)));
            assertEquals(60.0, data.getNorthBoundData(), 0);
            assertEquals(60.0, data.getSouthBoundData(), 0);
        });

        Map<String, Data<Double>> per20MinsData = eachDayData.getVehicleSpeedByPeriod(Period.MIN_20);
        assertEquals(72, per20MinsData.size());
        inc.set(0);
        long millisIn20Mins = Period.MIN_20.getPeriod();
        per20MinsData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisIn20Mins)));
            assertEquals(60.0, data.getNorthBoundData(), 0);
            assertEquals(60.0, data.getSouthBoundData(), 0);
        });

        Map<String, Data<Double>> perQuarterHourData = eachDayData.getVehicleSpeedByPeriod(Period.MIN_15);
        assertEquals(96, perQuarterHourData.size());
        inc.set(0);
        long millisInQtrHr = Period.MIN_15.getPeriod();
        perQuarterHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInQtrHr)));
            assertEquals(60.0, data.getNorthBoundData(), 0);
            assertEquals(60.0, data.getSouthBoundData(), 0);
        });
    }

    @Test
    public void testGetPeakVolumeTimeBySession() {
        EachDayData eachDayData = generateDataBy10Mins_IncludePeak();
        Data<String> morningSessionData = new Data("08:00-09:00", "07:00-08:00");
        Data<String> eveningSessionData = new Data("17:00-18:00", "16:00-17:00");
        assertEquals(morningSessionData, eachDayData.getPeakVolumeTimeBySession(Session.MORNING));
        assertEquals(eveningSessionData, eachDayData.getPeakVolumeTimeBySession(Session.EVENING));
    }

    @Test
    public void testGetDistanceBetweenVehiclesByPeriod() {
        EachDayData eachDayData = generateDataBy10Mins();

        AtomicLong inc = new AtomicLong();
        Map<String, Data<Double>> perHourData = eachDayData.getDistanceBetweenVehiclesByPeriod(Period.HOUR);
        assertEquals(24, perHourData.size());
        inc.set(0);
        long millisInHr = Period.HOUR.getPeriod();
        perHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInHr)));
            assertEquals(9997.5, data.getNorthBoundData(), 0);
            assertEquals(9997.5, data.getSouthBoundData(), 0);
        });

        Map<String, Data<Double>> perHalfHourData = eachDayData.getDistanceBetweenVehiclesByPeriod(Period.HALF_HOUR);
        assertEquals(48, perHalfHourData.size());
        inc.set(0);
        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        perHalfHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInHalfHr)));
            assertEquals(9997.5, data.getNorthBoundData(), 0);
            assertEquals(9997.5, data.getSouthBoundData(), 0);
        });

        Map<String, Data<Double>> per20MinsData = eachDayData.getDistanceBetweenVehiclesByPeriod(Period.MIN_20);
        assertEquals(72, per20MinsData.size());
        inc.set(0);
        long millisIn20Mins = Period.MIN_20.getPeriod();
        per20MinsData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisIn20Mins)));
            assertEquals(9997.5, data.getNorthBoundData(), 0);
            assertEquals(9997.5, data.getSouthBoundData(), 0);
        });

        Map<String, Data<Double>> perQuarterHourData = eachDayData.getDistanceBetweenVehiclesByPeriod(Period.MIN_15);
        assertEquals(96, perQuarterHourData.size());
        inc.set(0);
        long millisInQtrHr = Period.MIN_15.getPeriod();
        AtomicInteger index = new AtomicInteger(0);
        perQuarterHourData.forEach((timeFormat, data) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(inc.getAndAdd(millisInQtrHr)));
            if (index.getAndIncrement() % 2 == 0) {
                assertEquals(9997.5, data.getNorthBoundData(), 0);
                assertEquals(9997.5, data.getSouthBoundData(), 0);
            } else {
                assertEquals(0.0, data.getNorthBoundData(), 0);
                assertEquals(0.0, data.getSouthBoundData(), 0);
            }
        });
    }

    private EachDayData generateDataBy10Mins() {
        // Adding a north bound vehicle for every 10 mins for 24hrs.
        List<NorthBoundVehicle> northBoundVehicles = new ArrayList<>();
        long time = 0L;
        long millisIn10Min = 600000;
        long timeGap = 150; // This timegap with 2.5m axle gap results 60KMPH speed.
        for (int i = 1; i <= 144; i++) {
            northBoundVehicles.add(new NorthBoundVehicle(time, time + timeGap, 2.5));
            time = time + millisIn10Min;
        }

        // Adding a south bound vehicle for every 10 mins for 24hrs.
        List<SouthBoundVehicle> southBoundVehicles = new ArrayList<>();
        time = 0L;
        long hoseDiff = 4;
        for (int i = 1; i <= 144; i++) {
            southBoundVehicles.add(new SouthBoundVehicle(time, time + hoseDiff, time + timeGap,
                    time + timeGap + hoseDiff, 2.5));
            time = time + millisIn10Min;
        }

        EachDayData eachDayData = new EachDayData();
        eachDayData.getNorthBoundVehicles().addAll(northBoundVehicles);
        eachDayData.getSouthBoundVehicles().addAll(southBoundVehicles);
        return eachDayData;
    }

    private EachDayData generateDataBy10Mins_IncludePeak() {
        // Adding a north bound vehicle for every 10 mins for 24hrs.
        List<NorthBoundVehicle> northBoundVehicles = new ArrayList<>();
        long time = 0L;
        long millisIn10Min = 600000;
        long millisIn5Min = 300000;
        long timeGap = 150; // This timegap with 2.5m axle gap results 60KMPH speed.
        for (int i = 1; i <= 168; i++) {
            northBoundVehicles.add(new NorthBoundVehicle(time, time + timeGap, 2.5));
            // Adding extra vehicles between 08:00-09:00 and 17:00-18:00
            if ((i >= 48 && i < 60) || (i >= 108 && i < 120)) {
                time = time + millisIn5Min;
            } else {
                time = time + millisIn10Min;
            }
        }

        // Adding a south bound vehicle for every 10 mins for 24hrs.
        List<SouthBoundVehicle> southBoundVehicles = new ArrayList<>();
        time = 0L;
        long hoseDiff = 4;
        for (int i = 1; i <= 168; i++) {
            southBoundVehicles.add(new SouthBoundVehicle(time, time + hoseDiff, time + timeGap,
                    time + timeGap + hoseDiff, 2.5));
            // Adding extra vehicles between 07:00-08:00 and 16:00-17:00
            if ((i >= 42 && i < 54) || (i >= 102 && i < 114)) {
                time = time + millisIn5Min;
            } else {
                time = time + millisIn10Min;
            }
        }

        EachDayData eachDayData = new EachDayData();
        eachDayData.getNorthBoundVehicles().addAll(northBoundVehicles);
        eachDayData.getSouthBoundVehicles().addAll(southBoundVehicles);
        return eachDayData;
    }
}
