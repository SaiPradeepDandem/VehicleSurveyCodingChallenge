/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import com.sai.vehiclesurvey.data.Data;
import com.sai.vehiclesurvey.data.Period;
import com.sai.vehiclesurvey.data.Session;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static com.sai.vehiclesurvey.utils.SurveyUtils.*;

/**
 * Data object to hold the complete survey data.
 *
 * @author sai.dandem
 */
public class SurveyData {

    private final List<EachDayData> days = new ArrayList<>();

    public List<EachDayData> getDays() {
        return days;
    }

    /**
     * Returns the count of vehicles by the provided session for each day.
     *
     * @param session Session details.
     * @return Map for each day number with Data object holding the count of NB and SB vehicles.
     */
    public Map<Integer, Data<Long>> getVehicleCountBySession(Session session) {
        Map<Integer, Data<Long>> map = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            map.put((i + 1), days.get(i).getVehicleCountBySession(session));
        }
        return map;
    }

    /**
     * Returns the average count of vehicles by the provided session for all days.
     *
     * @param session Session details.
     * @return Data object holding the average count of NB and SB vehicles for all days.
     */
    public Data<Long> getAvgVehicleCountBySession(Session session) {
        Long totalNBCount = 0L;
        Long totalSBCount = 0L;
        for (int i = 0; i < days.size(); i++) {
            Data<Long> data = days.get(i).getVehicleCountBySession(session);
            totalNBCount = totalNBCount + data.getNorthBoundData();
            totalSBCount = totalSBCount + data.getSouthBoundData();
        }
        return new Data((long)Math.ceil((double)totalNBCount / days.size()), (long)Math.ceil(
                (double)totalSBCount / days.size()));
    }

    /**
     * Returns the count of vehicles by the provided period for each day.
     *
     * @param period Time period details.
     * @return Map for each day number with Data object holding the count of NB and SB vehicles.
     */
    public Map<Integer, Map<String, Data<Long>>> getVehicleCountByPeriod(Period period) {
        Map<Integer, Map<String, Data<Long>>> map = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            map.put((i + 1), days.get(i).getVehicleCountByPeriod(period));
        }
        return map;
    }

    /**
     * Returns the average count of vehicles by the provided period for all days.
     *
     * @param period Time period details.
     * @return Map for each day number with Data object holding the count of NB and SB vehicles.
     */
    public Map<String, Data<Long>> getAvgVehicleCountByPeriod(Period period) {
        Map<String, Data<Long>> avgData = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            Map<String, Data<Long>> data = days.get(i).getVehicleCountByPeriod(period);
            data.forEach((format, periodData) -> {
                if (avgData.get(format) == null) {
                    avgData.put(format, periodData);
                } else {
                    Data<Long> cache = avgData.get(format);
                    cache.setNorthBoundData(cache.getNorthBoundData() + periodData.getNorthBoundData());
                    cache.setSouthBoundData(cache.getSouthBoundData() + periodData.getSouthBoundData());
                }
            });
        }
        avgData.forEach((format, periodData) -> {
            periodData.setNorthBoundData((long)Math.ceil((double)periodData.getNorthBoundData() / days.size()));
            periodData.setSouthBoundData((long)Math.ceil((double)periodData.getSouthBoundData() / days.size()));
        });
        return avgData;
    }

    /**
     * Returns the speed of vehicles by the provided session for each day.
     *
     * @param session Session details.
     * @return Map for each day number with Data object holding the speed of NB and SB vehicles.
     */
    public Map<Integer, Data<Double>> getVehicleSpeedBySession(Session session) {
        Map<Integer, Data<Double>> map = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            map.put((i + 1), days.get(i).getVehicleSpeedBySession(session));
        }
        return map;
    }

    /**
     * Returns the average speed of vehicles by the provided session for all days.
     *
     * @param session Session details.
     * @return Data object holding the average speed of NB and SB vehicles for all days.
     */
    public Data<Double> getAvgVehicleSpeedBySession(Session session) {
        Double totalNBSpeed = 0.0;
        Double totalSBSpeed = 0.0;
        for (int i = 0; i < days.size(); i++) {
            Data<Double> data = days.get(i).getVehicleSpeedBySession(session);
            totalNBSpeed = totalNBSpeed + data.getNorthBoundData();
            totalSBSpeed = totalSBSpeed + data.getSouthBoundData();
        }
        return new Data(Math.ceil((double)totalNBSpeed / days.size()), Math.ceil((double)totalSBSpeed / days.size()));
    }

    /**
     * Returns the speed of vehicles by the provided period for each day.
     *
     * @param period Time period details.
     * @return Map for each day number with Data object holding the speed of NB and SB vehicles.
     */
    public Map<Integer, Map<String, Data<Double>>> getVehicleSpeedByPeriod(Period period) {
        Map<Integer, Map<String, Data<Double>>> map = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            map.put((i + 1), days.get(i).getVehicleSpeedByPeriod(period));
        }
        return map;
    }

    /**
     * Returns the average speed of vehicles by the provided period for all days.
     *
     * @param period Time period details.
     * @return Map for each day number with Data object holding the speed of NB and SB vehicles.
     */
    public Map<String, Data<Double>> getAvgVehicleSpeedByPeriod(Period period) {
        Map<String, Data<Double>> avgData = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            Map<String, Data<Double>> data = days.get(i).getVehicleSpeedByPeriod(period);
            data.forEach((format, periodData) -> {
                if (avgData.get(format) == null) {
                    avgData.put(format, periodData);
                } else {
                    Data<Double> cache = avgData.get(format);
                    cache.setNorthBoundData(cache.getNorthBoundData() + periodData.getNorthBoundData());
                    cache.setSouthBoundData(cache.getSouthBoundData() + periodData.getSouthBoundData());
                }
            });
        }
        avgData.forEach((format, periodData) -> {
            periodData.setNorthBoundData(Math.ceil((double)periodData.getNorthBoundData() / days.size()));
            periodData.setSouthBoundData(Math.ceil((double)periodData.getSouthBoundData() / days.size()));
        });
        return avgData;
    }

    /**
     * Returns the peak hour details of vehicles by the provided session for each day.
     *
     * @param session Session details.
     * @return Map for each day number with Data object holding the peak hour of NB and SB vehicles.
     */
    public Map<Integer, Data<String>> getPeakVolumeTimeBySession(Session session) {
        Map<Integer, Data<String>> map = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            map.put((i + 1), days.get(i).getPeakVolumeTimeBySession(session));
        }
        return map;
    }

    /**
     * Returns the average distance between vehicles by the provided period for all days.
     *
     * @param period Time period details.
     * @return Map for each day number with Data object holding the distance between NB and SB vehicles.
     */
    public Map<String, Data<Double>> getAvgDistanceBetweenVehicles_ByPeriod(Period period) {
        Map<String, Data<Double>> avgData = new LinkedHashMap<>();
        for (int i = 0; i < days.size(); i++) {
            Map<String, Data<Double>> data = days.get(i).getDistanceBetweenVehiclesByPeriod(period);
            data.forEach((format, periodData) -> {
                if (avgData.get(format) == null) {
                    avgData.put(format, periodData);
                } else {
                    Data<Double> cache = avgData.get(format);
                    cache.setNorthBoundData(cache.getNorthBoundData() + periodData.getNorthBoundData());
                    cache.setSouthBoundData(cache.getSouthBoundData() + periodData.getSouthBoundData());
                }
            });
        }
        avgData.forEach((format, periodData) -> {
            periodData.setNorthBoundData(round((double)periodData.getNorthBoundData() / days.size(), 2));
            periodData.setSouthBoundData(round((double)periodData.getSouthBoundData() / days.size(), 2));
        });
        return avgData;
    }
}
