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
 * Test class for SurveyData model object.
 *
 * @author sai.dandem
 */
public class SurveyDataTest {

    private SurveyData generateDataBy10MinsFor5Days() {
        SurveyData data = new SurveyData();
        for (int k = 1; k <= 5; k++) {
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
            data.getDays().add(eachDayData);
        }

        return data;
    }

    private SurveyData generateDataBy10MinsFor5Days_IncludePeak() {
        SurveyData data = new SurveyData();
        for (int k = 1; k <= 5; k++) {
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
            data.getDays().add(eachDayData);
        }

        return data;
    }

    @Test
    public void testGetVehicleCountBySession() {
        final SurveyData data = generateDataBy10MinsFor5Days();
        final AtomicInteger num = new AtomicInteger(1);
        final Map<Integer, Data<Long>> morningSessionData = data.getVehicleCountBySession(Session.MORNING);
        assertEquals(5, morningSessionData.size());
        final Data<Long> msd = new Data(36L, 36L);
        morningSessionData.forEach((dayNum, countData) -> {
            assertEquals(num.getAndIncrement(), dayNum.intValue());
            assertEquals(msd, countData);
        });

        num.set(1);
        final Map<Integer, Data<Long>> eveningSessionData = data.getVehicleCountBySession(Session.EVENING);
        assertEquals(5, eveningSessionData.size());
        final Data<Long> esd = new Data(36L, 36L);
        eveningSessionData.forEach((dayNum, countData) -> {
            assertEquals(num.getAndIncrement(), dayNum.intValue());
            assertEquals(esd, countData);
        });
    }

    @Test
    public void testGetAvgVehicleCountBySession() {
        final SurveyData data = generateDataBy10MinsFor5Days();
        final Data<Long> avgMorningSessionData = data.getAvgVehicleCountBySession(Session.MORNING);
        assertEquals(new Data(36L, 36L), avgMorningSessionData);

        final Data<Long> avgEveningSessionData = data.getAvgVehicleCountBySession(Session.EVENING);
        assertEquals(new Data(36L, 36L), avgEveningSessionData);
    }

    @Test
    public void testGetVehicleCountByPeriod() {
        final SurveyData data = generateDataBy10MinsFor5Days();
        final AtomicInteger dayNumInc = new AtomicInteger(1);
        final AtomicLong periodInc = new AtomicLong();

        long millisInHr = Period.HOUR.getPeriod();
        Map<Integer, Map<String, Data<Long>>> perHourData = data.getVehicleCountByPeriod(Period.HOUR);
        assertEquals(5, perHourData.size());
        perHourData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(24, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, hourData) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHr)));
                assertEquals(6L, hourData.getNorthBoundData().longValue());
                assertEquals(6L, hourData.getSouthBoundData().longValue());
            });
        });

        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        dayNumInc.set(1);
        Map<Integer, Map<String, Data<Long>>> perHalfHourData = data.getVehicleCountByPeriod(Period.HALF_HOUR);
        assertEquals(5, perHalfHourData.size());
        perHalfHourData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(48, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, halfHourData) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHalfHr)));
                assertEquals(3L, halfHourData.getNorthBoundData().longValue());
                assertEquals(3L, halfHourData.getSouthBoundData().longValue());
            });
        });

        long millisIn20Mins = Period.MIN_20.getPeriod();
        dayNumInc.set(1);
        Map<Integer, Map<String, Data<Long>>> per20MinsData = data.getVehicleCountByPeriod(Period.MIN_20);
        assertEquals(5, per20MinsData.size());
        per20MinsData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(72, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, min20Data) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisIn20Mins)));
                assertEquals(2L, min20Data.getNorthBoundData().longValue());
                assertEquals(2L, min20Data.getSouthBoundData().longValue());
            });
        });

        long millisInQtrHr = Period.MIN_15.getPeriod();
        dayNumInc.set(1);
        Map<Integer, Map<String, Data<Long>>> perQuarterHourData = data.getVehicleCountByPeriod(Period.MIN_15);
        assertEquals(5, perQuarterHourData.size());
        perQuarterHourData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(96, dataMap.size());
            periodInc.set(0);
            AtomicInteger index = new AtomicInteger(0);
            dataMap.forEach((timeFormat, qtrHourData) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInQtrHr)));
                if (index.getAndIncrement() % 2 == 0) {
                    assertEquals(2L, qtrHourData.getNorthBoundData().longValue());
                    assertEquals(2L, qtrHourData.getSouthBoundData().longValue());
                } else {
                    assertEquals(1L, qtrHourData.getNorthBoundData().longValue());
                    assertEquals(1L, qtrHourData.getSouthBoundData().longValue());
                }
            });
        });
    }

    @Test
    public void testGetAvgVehicleCountByPeriod() {
        final AtomicLong periodInc = new AtomicLong();
        final SurveyData data = generateDataBy10MinsFor5Days();

        long millisInHr = Period.HOUR.getPeriod();
        final Map<String, Data<Long>> perHourData = data.getAvgVehicleCountByPeriod(Period.HOUR);
        assertEquals(24, perHourData.size());
        periodInc.set(0);
        perHourData.forEach((timeFormat, hourData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHr)));
            assertEquals(6L, hourData.getNorthBoundData().longValue());
            assertEquals(6L, hourData.getSouthBoundData().longValue());
        });

        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        final Map<String, Data<Long>> perHalfHourData = data.getAvgVehicleCountByPeriod(Period.HALF_HOUR);
        assertEquals(48, perHalfHourData.size());
        periodInc.set(0);
        perHalfHourData.forEach((timeFormat, halfHrData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHalfHr)));
            assertEquals(3L, halfHrData.getNorthBoundData().longValue());
            assertEquals(3L, halfHrData.getSouthBoundData().longValue());
        });

        long millisIn20Mins = Period.MIN_20.getPeriod();
        final Map<String, Data<Long>> per20MinsData = data.getAvgVehicleCountByPeriod(Period.MIN_20);
        assertEquals(72, per20MinsData.size());
        periodInc.set(0);
        per20MinsData.forEach((timeFormat, data20Mins) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisIn20Mins)));
            assertEquals(2L, data20Mins.getNorthBoundData().longValue());
            assertEquals(2L, data20Mins.getSouthBoundData().longValue());
        });

        long millisInQtrHr = Period.MIN_15.getPeriod();
        final Map<String, Data<Long>> perQuarterHourData = data.getAvgVehicleCountByPeriod(Period.MIN_15);
        assertEquals(96, perQuarterHourData.size());
        periodInc.set(0);
        AtomicInteger index = new AtomicInteger(0);
        perQuarterHourData.forEach((timeFormat, qtrHrData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInQtrHr)));
            if (index.getAndIncrement() % 2 == 0) {
                assertEquals(2L, qtrHrData.getNorthBoundData().longValue());
                assertEquals(2L, qtrHrData.getSouthBoundData().longValue());
            } else {
                assertEquals(1L, qtrHrData.getNorthBoundData().longValue());
                assertEquals(1L, qtrHrData.getSouthBoundData().longValue());
            }
        });
    }

    @Test
    public void testVehicleSpeedBySessionn() {
        final SurveyData data = generateDataBy10MinsFor5Days();
        final AtomicInteger num = new AtomicInteger(1);
        final Map<Integer, Data<Double>> morningSessionData = data.getVehicleSpeedBySession(Session.MORNING);
        assertEquals(5, morningSessionData.size());
        final Data<Long> msd = new Data(60.0, 60.0);
        morningSessionData.forEach((dayNum, countData) -> {
            assertEquals(num.getAndIncrement(), dayNum.intValue());
            assertEquals(msd, countData);
        });

        num.set(1);
        final Map<Integer, Data<Double>> eveningSessionData = data.getVehicleSpeedBySession(Session.EVENING);
        assertEquals(5, eveningSessionData.size());
        final Data<Long> esd = new Data(60.0, 60.0);
        eveningSessionData.forEach((dayNum, countData) -> {
            assertEquals(num.getAndIncrement(), dayNum.intValue());
            assertEquals(esd, countData);
        });
    }

    @Test
    public void testGetAvgVehicleSpeedBySession() {
        final SurveyData data = generateDataBy10MinsFor5Days();
        final Data<Double> avgMorningSessionData = data.getAvgVehicleSpeedBySession(Session.MORNING);
        assertEquals(new Data(60.0, 60.0), avgMorningSessionData);

        final Data<Double> avgEveningSessionData = data.getAvgVehicleSpeedBySession(Session.EVENING);
        assertEquals(new Data(60.0, 60.0), avgEveningSessionData);
    }

    @Test
    public void testGetVehicleSpeedByPeriod() {
        final SurveyData data = generateDataBy10MinsFor5Days();
        final AtomicInteger dayNumInc = new AtomicInteger(1);
        final AtomicLong periodInc = new AtomicLong();

        long millisInHr = Period.HOUR.getPeriod();
        Map<Integer, Map<String, Data<Double>>> perHourData = data.getVehicleSpeedByPeriod(Period.HOUR);
        assertEquals(5, perHourData.size());
        perHourData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(24, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, hourData) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHr)));
                assertEquals(60.0, hourData.getNorthBoundData(), 0);
                assertEquals(60.0, hourData.getSouthBoundData(), 0);
            });
        });

        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        dayNumInc.set(1);
        Map<Integer, Map<String, Data<Double>>> perHalfHourData = data.getVehicleSpeedByPeriod(Period.HALF_HOUR);
        assertEquals(5, perHalfHourData.size());
        perHalfHourData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(48, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, halfHourData) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHalfHr)));
                assertEquals(60.0, halfHourData.getNorthBoundData(), 0);
                assertEquals(60.0, halfHourData.getSouthBoundData(), 0);
            });
        });

        long millisIn20Mins = Period.MIN_20.getPeriod();
        dayNumInc.set(1);
        Map<Integer, Map<String, Data<Double>>> per20MinData = data.getVehicleSpeedByPeriod(Period.MIN_20);
        assertEquals(5, per20MinData.size());
        per20MinData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(72, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, data20Min) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisIn20Mins)));
                assertEquals(60.0, data20Min.getNorthBoundData(), 0);
                assertEquals(60.0, data20Min.getSouthBoundData(), 0);
            });
        });

        long millisInQtrHr = Period.MIN_15.getPeriod();
        dayNumInc.set(1);
        Map<Integer, Map<String, Data<Double>>> perQtrHourData = data.getVehicleSpeedByPeriod(Period.MIN_15);
        assertEquals(5, perQtrHourData.size());
        perQtrHourData.forEach((dayNum, dataMap) -> {
            assertEquals(dayNumInc.getAndIncrement(), dayNum.intValue());
            assertEquals(96, dataMap.size());
            periodInc.set(0);
            dataMap.forEach((timeFormat, qtrHourData) -> {
                assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInQtrHr)));
                assertEquals(60.0, qtrHourData.getNorthBoundData(), 0);
                assertEquals(60.0, qtrHourData.getSouthBoundData(), 0);
            });
        });
    }

    @Test
    public void testGetAvgVehicleSpeedByPeriod() {
        final AtomicLong periodInc = new AtomicLong();
        final SurveyData data = generateDataBy10MinsFor5Days();

        long millisInHr = Period.HOUR.getPeriod();
        final Map<String, Data<Double>> perHourData = data.getAvgVehicleSpeedByPeriod(Period.HOUR);
        assertEquals(24, perHourData.size());
        periodInc.set(0);
        perHourData.forEach((timeFormat, hourData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHr)));
            assertEquals(60.0, hourData.getNorthBoundData(), 0);
            assertEquals(60.0, hourData.getSouthBoundData(), 0);
        });

        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        final Map<String, Data<Double>> perHalfHourData = data.getAvgVehicleSpeedByPeriod(Period.HALF_HOUR);
        assertEquals(48, perHalfHourData.size());
        periodInc.set(0);
        perHalfHourData.forEach((timeFormat, halfHrData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHalfHr)));
            assertEquals(60.0, halfHrData.getNorthBoundData(), 0);
            assertEquals(60.0, halfHrData.getSouthBoundData(), 0);
        });

        long millisIn20Mins = Period.MIN_20.getPeriod();
        final Map<String, Data<Double>> per20MinsData = data.getAvgVehicleSpeedByPeriod(Period.MIN_20);
        assertEquals(72, per20MinsData.size());
        periodInc.set(0);
        per20MinsData.forEach((timeFormat, data20Mins) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisIn20Mins)));
            assertEquals(60.0, data20Mins.getNorthBoundData(), 0);
            assertEquals(60.0, data20Mins.getSouthBoundData(), 0);
        });

        long millisInQtrHr = Period.MIN_15.getPeriod();
        final Map<String, Data<Double>> perQuarterHourData = data.getAvgVehicleSpeedByPeriod(Period.MIN_15);
        assertEquals(96, perQuarterHourData.size());
        periodInc.set(0);
        perQuarterHourData.forEach((timeFormat, qtrHrData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInQtrHr)));
            assertEquals(60.0, qtrHrData.getNorthBoundData(), 0);
            assertEquals(60.0, qtrHrData.getSouthBoundData(), 0);
        });
    }

    @Test
    public void testGetPeakVolumeTimeBySession() {
        final SurveyData data = generateDataBy10MinsFor5Days_IncludePeak();
        final AtomicInteger num = new AtomicInteger(1);
        final Map<Integer, Data<String>> morningSessionData = data.getPeakVolumeTimeBySession(Session.MORNING);
        assertEquals(5, morningSessionData.size());
        final Data<String> msd = new Data("08:00-09:00", "07:00-08:00");
        morningSessionData.forEach((dayNum, peakData) -> {
            assertEquals(num.getAndIncrement(), dayNum.intValue());
            assertEquals(msd, peakData);
        });

        num.set(1);
        final Map<Integer, Data<String>> eveningSessionData = data.getPeakVolumeTimeBySession(Session.EVENING);
        assertEquals(5, eveningSessionData.size());
        final Data<String> esd = new Data("17:00-18:00", "16:00-17:00");
        eveningSessionData.forEach((dayNum, peakData) -> {
            assertEquals(num.getAndIncrement(), dayNum.intValue());
            assertEquals(esd, peakData);
        });
    }

    @Test
    public void testGetAvgDistanceBetweenVehicles_ByPeriod() {
        final AtomicLong periodInc = new AtomicLong();
        final SurveyData data = generateDataBy10MinsFor5Days();

        long millisInHr = Period.HOUR.getPeriod();
        final Map<String, Data<Double>> perHourData = data.getAvgDistanceBetweenVehicles_ByPeriod(Period.HOUR);
        assertEquals(24, perHourData.size());
        periodInc.set(0);
        perHourData.forEach((timeFormat, hourData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHr)));
            assertEquals(9997.5, hourData.getNorthBoundData(), 0);
            assertEquals(9997.5, hourData.getSouthBoundData(), 0);
        });

        long millisInHalfHr = Period.HALF_HOUR.getPeriod();
        final Map<String, Data<Double>> perHalfHourData = data.getAvgDistanceBetweenVehicles_ByPeriod(Period.HALF_HOUR);
        assertEquals(48, perHalfHourData.size());
        periodInc.set(0);
        perHalfHourData.forEach((timeFormat, halfHrData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInHalfHr)));
            assertEquals(9997.5, halfHrData.getNorthBoundData(), 0);
            assertEquals(9997.5, halfHrData.getSouthBoundData(), 0);
        });

        long millisIn20Mins = Period.MIN_20.getPeriod();
        final Map<String, Data<Double>> per20MinsData = data.getAvgDistanceBetweenVehicles_ByPeriod(Period.MIN_20);
        assertEquals(72, per20MinsData.size());
        periodInc.set(0);
        per20MinsData.forEach((timeFormat, data20Mins) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisIn20Mins)));
            assertEquals(9997.5, data20Mins.getNorthBoundData(), 0);
            assertEquals(9997.5, data20Mins.getSouthBoundData(), 0);
        });

        long millisInQtrHr = Period.MIN_15.getPeriod();
        final Map<String, Data<Double>> perQuarterHourData = data.getAvgDistanceBetweenVehicles_ByPeriod(Period.MIN_15);
        assertEquals(96, perQuarterHourData.size());
        periodInc.set(0);
        AtomicInteger index = new AtomicInteger(0);
        perQuarterHourData.forEach((timeFormat, qtrHrData) -> {
            assertEquals(timeFormat, SurveyUtils.convertToHHMM(periodInc.getAndAdd(millisInQtrHr)));
            if (index.getAndIncrement() % 2 == 0) {
                assertEquals(9997.5, qtrHrData.getNorthBoundData(), 0);
                assertEquals(9997.5, qtrHrData.getSouthBoundData(), 0);
            } else {
                assertEquals(0.0, qtrHrData.getNorthBoundData(), 0);
                assertEquals(0.0, qtrHrData.getSouthBoundData(), 0);
            }
        });
    }
}
