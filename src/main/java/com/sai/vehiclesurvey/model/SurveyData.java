/*
 *  Copyright © - Sai Pradeep Dandem. All rights reserved.
 */
package com.sai.vehiclesurvey.model;

import java.util.ArrayList;
import java.util.List;

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

}
