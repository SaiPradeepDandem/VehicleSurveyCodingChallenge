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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class to parse and analyze the vehicle survey data.
 *
 * @author sai.dandem
 */
public class AnalyzeVehicleSurvey {

    private SurveyData surveyData = null;

    public SurveyData getSurveyData() {
        return surveyData;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        AnalyzeVehicleSurvey obj = new AnalyzeVehicleSurvey();
        List<String> readings = obj.readDataFile();
        obj.parseData(readings);
        System.out.println("Days ::" + obj.getSurveyData().getDays().size());
        for (int i = 0; i < obj.getSurveyData().getDays().size(); i++) {
            EachDayData dayData = obj.getSurveyData().getDays().get(i);
            System.out.println("DAY "+(i+1));
            System.out.println("North Bound Vehicles :: "+dayData.getNorthBoundVehicles().size());
            System.out.println("South Bound Vehicles :: "+dayData.getSouthBoundVehicles().size());
            System.out.println("");
        }
    }

    /**
     * Read the data file from the resources and provide the contents as list of
     * strings.
     *
     * @return List of readings (strings).
     * @throws URISyntaxException
     * @throws IOException
     */
    public List<String> readDataFile() throws URISyntaxException, IOException {
        Path dataFile = Paths.get(AnalyzeVehicleSurvey.class.getResource("E:\\Projects\\VehicleSurveyCodingChallenge\\src\\main\\resources\\data\\sampledata.txt").toURI());
        return Files.readAllLines(dataFile);
    }

    /**
     * Parses the given list of data into the SurveyData object.
     *
     * @param data List of readings (strings).
     */
    public void parseData(List<String> data) {
        // Instantiating the core survey data object.
        surveyData = new SurveyData();

        DataParser parser = new DataParser();
        for (int i = 0; i < data.size(); i++) {
            String reading = data.get(i);
            String nextReading = (i < (data.size() - 1)) ? data.get(i + 1) : null;
            parser.parse(reading, nextReading);
        }

        // Checking if the parser has any unfinished data.
        if (!parser.isEmpty()) {
            throw new IllegalStateException("Not a valid data");
        }
    }

    /**
     * Class to parse the provided reading and push into the appropriate position.
     */
    class DataParser {

        private EachDayData dayData = new EachDayData();

        private long lastMax = 0;

        private final List<NorthBoundVehicle> nbvs = new ArrayList<>();

        private final List<SouthBoundVehicle> sbvs = new ArrayList<>();

        public void parse(String reading, String nextReading) {
            if (surveyData.getDays().isEmpty()) {
                surveyData.getDays().add(dayData);
            }
            checkDayChanged(reading);
            if (reading.startsWith("A")) {
                if (nextReading != null && nextReading.startsWith("B")) {
                    loadInSB(reading);
                } else {
                    loadInNB(reading);
                }
            } else {
                loadInSB(reading);
            }
        }

        /**
         * Loads the reading into NorthBoundVehicle.
         *
         * @param reading Reading to be pushed.
         */
        private void loadInNB(String reading) {
            if (nbvs.isEmpty()) {
                nbvs.add(new NorthBoundVehicle());
            }
            if (nbvs.get(0).isFull()) {
                throw new IllegalStateException("Cannot be a full reading north bound vehicle.");
            } else {
                long readingValue = Long.parseLong(reading.substring(1));
                if (nbvs.get(0).getHoseAFirstRead() == null) {
                    nbvs.get(0).setHoseAFirstRead(readingValue);
                } else {
                    if (readingValue > lastMax) {
                        lastMax = readingValue;
                    }
                    nbvs.get(0).setHoseASecondRead(readingValue);
                    dayData.getNorthBoundVehicles().add(nbvs.get(0));
                    nbvs.remove(0);
                }
            }
        }

        /**
         * Loads the reading into SouthBoundVehicle.
         *
         * @param reading Reading to be pushed.
         */
        private void loadInSB(String reading) {
            if (sbvs.isEmpty()) {
                sbvs.add(new SouthBoundVehicle());
            }
            long readingValue = Long.parseLong(reading.substring(1));
            if (sbvs.get(0).isFull()) {
                throw new IllegalStateException("Cannot be a full reading south bound vehicle.");
            } else {
                if (reading.startsWith("A")) {
                    if (sbvs.get(0).getHoseAFirstRead() == null) {
                        sbvs.get(0).setHoseAFirstRead(readingValue);
                    } else {
                        if (readingValue > lastMax) {
                            lastMax = readingValue;
                        }
                        sbvs.get(0).setHoseASecondRead(readingValue);
                    }
                } else {
                    if (sbvs.get(0).getHoseBFirstRead() == null) {
                        sbvs.get(0).setHoseBFirstRead(readingValue);
                    } else {
                        if (readingValue > lastMax) {
                            lastMax = readingValue;
                        }
                        sbvs.get(0).setHoseBSecondRead(readingValue);
                        dayData.getSouthBoundVehicles().add(sbvs.get(0));
                        sbvs.remove(0);
                    }
                }
            }
        }

        /**
         * Checks if the day is changed based on the reading value.
         *
         * @param reading Reading to be checked.
         */
        private void checkDayChanged(String reading) {
            long readingValue = Long.parseLong(reading.substring(1));
            if (readingValue < lastMax) {
                dayData = new EachDayData();
                surveyData.getDays().add(dayData);
                lastMax = 0;
            }
        }

        /**
         * Checks whether any of the data is present.
         *
         * @return true/false
         */
        public boolean isEmpty() {
            return nbvs.isEmpty() && sbvs.isEmpty();
        }
    }
}
