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
import java.util.stream.Collectors;
import static com.sai.vehiclesurvey.utils.SurveyUtils.*;

/**
 * Data object to hold the data of vehicles crossed in 24Hrs of each day.
 *
 * @author sai.dandem
 */
public class EachDayData {

    private final List<NorthBoundVehicle> northBoundVehicles = new ArrayList<>();

    private final List<SouthBoundVehicle> southBoundVehicles = new ArrayList<>();

    public List<NorthBoundVehicle> getNorthBoundVehicles() {
        return northBoundVehicles;
    }

    public List<SouthBoundVehicle> getSouthBoundVehicles() {
        return southBoundVehicles;
    }

    /**
     * Returns the count of vehicles by the provided session.
     *
     * @param session Session details.
     * @return Data object holding the count of NB and SB vehicles.
     */
    public Data<Long> getVehicleCountBySession(Session session) {
        long nbCount = northBoundVehicles.stream().filter(nbv -> nbv.isInSession(session)).count();
        long sbCount = southBoundVehicles.stream().filter(sbv -> sbv.isInSession(session)).count();
        return new Data<>(nbCount, sbCount);
    }

    /**
     * Returns the count of vehicles per the provided period in each day.
     *
     * @param period Period of time to count the data.
     * @return Map of period values with count data.
     */
    public Map<String, Data<Long>> getVehicleCountByPeriod(Period period) {
        Map<String, Data<Long>> ret = new LinkedHashMap<>();
        long millisInPeriod = period.getPeriod();
        long size = MILLIS_PER_24HRS / millisInPeriod;
        for (int i = 0; i < size; i++) {
            long start = i * millisInPeriod;
            long end = start + millisInPeriod - 1;
            long nbCount = northBoundVehicles.parallelStream().filter(nbv -> nbv.isInTimeRange(start, end)).count();
            long sbCount = southBoundVehicles.parallelStream().filter(sbv -> sbv.isInTimeRange(start, end)).count();
            ret.put(convertToHHMM(start), new Data<>(nbCount, sbCount));
        }
        return ret;
    }

    /**
     * Returns the average speed of vehicles by the provided session.
     *
     * @param session Session details.
     * @return Data object holding the average speed NB and SB vehicles.
     */
    public Data<Double> getVehicleSpeedBySession(Session session) {
        double nbAvgSpeed = northBoundVehicles.stream().filter(nbv -> nbv.isInSession(session))
                .mapToDouble(Vehicle::getSpeedInKMPH)
                .average().getAsDouble();
        double sbAvgSpeed = southBoundVehicles.stream().filter(sbv -> sbv.isInSession(session))
                .mapToDouble(Vehicle::getSpeedInKMPH)
                .average().getAsDouble();
        return new Data<>(round(nbAvgSpeed, 1), round(sbAvgSpeed, 1));
    }

    /**
     * Returns the average speed of vehicles per the provided period in each day.
     *
     * @param period Period of time to get the speed of data.
     * @return Map of period values with average speed data.
     */
    public Map<String, Data<Double>> getVehicleSpeedByPeriod(Period period) {
        Map<String, Data<Double>> ret = new LinkedHashMap<>();
        long millisInPeriod = period.getPeriod();
        long size = MILLIS_PER_24HRS / millisInPeriod;
        for (int i = 0; i < size; i++) {
            long start = i * millisInPeriod;
            long end = start + millisInPeriod - 1;
            List<NorthBoundVehicle> nbst = northBoundVehicles.parallelStream().filter(nbv -> nbv.isInTimeRange(
                    start, end)).collect(Collectors.toList());
            double nbAvgSpeed = 0;
            if (!nbst.isEmpty()) {
                nbAvgSpeed = nbst.stream().mapToDouble(Vehicle::getSpeedInKMPH).average().getAsDouble();
            }

            List<SouthBoundVehicle> sbst = southBoundVehicles.parallelStream().filter(sbv -> sbv.isInTimeRange(
                    start, end)).collect(Collectors.toList());
            double sbAvgSpeed = 0;
            if (!sbst.isEmpty()) {
                sbAvgSpeed = sbst.stream().mapToDouble(Vehicle::getSpeedInKMPH).average().getAsDouble();
            }

            ret.put(convertToHHMM(start), new Data<>(round(nbAvgSpeed, 1), round(sbAvgSpeed, 1)));
        }
        return ret;
    }

    /**
     * Determines the peak volume time in the given session. Peak volume give the hour range.
     *
     * @param session Session details.
     * @return Data object holding the peak hour details.
     */
    public Data<String> getPeakVolumeTimeBySession(Session session) {
        long millisInHour = Period.HOUR.getPeriod();
        long size = MILLIS_PER_24HRS / millisInHour;

        long nbMaxCount = 0;
        long nbMaxStart = 0;
        long sbMaxCount = 0;
        long sbMaxStart = 0;

        for (int i = 0; i < size; i++) {
            long start = i * millisInHour;
            long end = start + millisInHour - 1;

            if (session.containsPeriod(start, end)) {
                long nbCount = northBoundVehicles.parallelStream().filter(nbv -> nbv.isInTimeRange(start, end)).count();
                if (nbCount > nbMaxCount) {
                    nbMaxCount = nbCount;
                    nbMaxStart = start;
                }
                long sbCount = southBoundVehicles.parallelStream().filter(sbv -> sbv.isInTimeRange(start, end)).count();
                if (sbCount > sbMaxCount) {
                    sbMaxCount = sbCount;
                    sbMaxStart = start;
                }
            }
        }
        return new Data<>(convertToHHMM(nbMaxStart) + "-" + convertToHHMM(nbMaxStart + millisInHour),
                convertToHHMM(sbMaxStart) + "-" + convertToHHMM(sbMaxStart + millisInHour));
    }

    /**
     * Returns the average distance between vehicles per the provided period in each day.
     *
     * @param period Period of time to get the distance of data.
     * @return Map of period values with distance between vehicles data.
     */
    public Map<String, Data<Double>> getDistanceBetweenVehiclesByPeriod(Period period) {
        Map<String, Data<Double>> ret = new LinkedHashMap<>();
        long millisInPeriod = period.getPeriod();
        long size = MILLIS_PER_24HRS / millisInPeriod;
        for (int i = 0; i < size; i++) {
            long start = i * millisInPeriod;
            long end = start + millisInPeriod - 1;

            List<NorthBoundVehicle> nbvs = northBoundVehicles.stream()
                    .filter(nbv -> nbv.isInTimeRange(start, end)).collect(Collectors.toList());
            double nbTotal = 0;
            double nbCount = 0;
            for (int j = 1; j < nbvs.size(); j++) {
                nbTotal = nbTotal + nbvs.get(j).findDistanceInMts_FromFrontVehicle(nbvs.get(j - 1));
                nbCount++;
            }

            List<SouthBoundVehicle> sbvs = southBoundVehicles.stream()
                    .filter(sbv -> sbv.isInTimeRange(start, end)).collect(Collectors.toList());
            double sbTotal = 0;
            double sbCount = 0;
            for (int j = 1; j < sbvs.size(); j++) {
                sbTotal = sbTotal + sbvs.get(j).findDistanceInMts_FromFrontVehicle(sbvs.get(j - 1));
                sbCount++;
            }

            ret.put(convertToHHMM(start), new Data<>(round(nbTotal / nbCount, 1),
                    round(sbTotal / sbCount, 1)));
        }
        return ret;
    }

}
