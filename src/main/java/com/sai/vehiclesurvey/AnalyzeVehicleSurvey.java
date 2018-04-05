/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey;

import com.sai.vehiclesurvey.model.SurveyData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * Read the data file from the resources and provide the contents as list of strings.
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
        long nbFrontHoseA = 0, nbBackHoseA = 0, sbFrontHoseA = 0, sbBackHoseA = 0, sbFrontHoseB = 0, sbBackHoseB = 0;
        for (int i = 0; i < data.size(); i++) {
            String reading = data.get(i);
            System.out.println(nbBackHoseA);
        }

    }

}
