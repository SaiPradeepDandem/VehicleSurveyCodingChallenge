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

    /**
     * Read the data file from the resources and provide the contents as list of
     * strings.
     *
     * @return List of readings (strings).
     * @throws URISyntaxException
     * @throws IOException
     */
    public List<String> readDataFile() throws URISyntaxException, IOException {
        Path dataFile = Paths.get(getClass().getResource("/data/sampledata.txt").toURI());
        return Files.readAllLines(dataFile);
    }

    /**
     * Parses the given list of data into the SurveyData object.
     *
     * @param data List of readings (strings).
     */
    public void parseData(List<String> data) {
        surveyData = new SurveyData();
        surveyData.getDays().add(new EachDayData());
        DataParser parser = new DataParser();
        for (int i = 0; i < data.size(); i++) {
            String reading = data.get(i);
            String nextReading = (i < (data.size() - 1)) ? data.get(i + 1) : null;
            parser.parse(reading, nextReading);
        }

        if (!parser.isEmpty()) {
            throw new IllegalStateException("Not a valid data");
        }
    }

    class DataParser {

        private final List<NorthBoundVehicle> nbvs = new ArrayList<>();
        private final List<SouthBoundVehicle> sbvs = new ArrayList<>();

        public void parse(String reading, String nextReading) {
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

        private void loadInNB(String reading) {
            if (nbvs.isEmpty()) {
                nbvs.add(new NorthBoundVehicle());
            }
            if (nbvs.get(0).isFull()) {
                throw new IllegalStateException("Cannot be a full reading north bound vehicle.");
            } else {
                if (nbvs.get(0).getHoseAFirstRead() == null) {
                    nbvs.get(0).setHoseAFirstRead(Long.parseLong(reading.substring(1)));
                } else {
                    nbvs.get(0).setHoseASecondRead(Long.parseLong(reading.substring(1)));
                    surveyData.getDays().get(0).getNorthBoundVehicles().add(nbvs.get(0));
                    nbvs.remove(0);
                }
            }
        }

        private void loadInSB(String reading) {
            if (sbvs.isEmpty()) {
                sbvs.add(new SouthBoundVehicle());
            }
            if (sbvs.get(0).isFull()) {
                throw new IllegalStateException("Cannot be a full reading south bound vehicle.");
            } else {
                if (reading.startsWith("A")) {
                    if (sbvs.get(0).getHoseAFirstRead() == null) {
                        sbvs.get(0).setHoseAFirstRead(Long.parseLong(reading.substring(1)));
                    } else {
                        sbvs.get(0).setHoseASecondRead(Long.parseLong(reading.substring(1)));
                    }
                } else {
                    if (sbvs.get(0).getHoseBFirstRead() == null) {
                        sbvs.get(0).setHoseBFirstRead(Long.parseLong(reading.substring(1)));
                    } else {
                        sbvs.get(0).setHoseBSecondRead(Long.parseLong(reading.substring(1)));
                        surveyData.getDays().get(0).getSouthBoundVehicles().add(sbvs.get(0));
                        sbvs.remove(0);
                    }
                }
            }
        }

        public boolean isEmpty() {
            return nbvs.isEmpty() && sbvs.isEmpty();
        }
    }
}
