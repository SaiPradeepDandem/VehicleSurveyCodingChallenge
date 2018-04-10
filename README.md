# VehicleSurveyCodingChallenge
A Java based code challenge to write a program for vehicle survey particularly focusing on "Test Driven Developement" as major component.

## Design & Approach:
The primary reason for choosing this challenge is because of its complexity & scope for logical implementation. There is considerbly a good scope for implementing OOPS concepts for this challenge. This challenge is done completely only using Java SDK and JUnit library.

Test Driven Developement is the major component for this challenge. All the logic/code is developed using the test data that is run/tested through JUnit test cases.

All the survey data that is provided in the file, is parsed and loaded into appropriate data objects. Each data object holds it own data analysing logic. And the analysis of survey data is done by calling the approriate getter methods of main data object "SurveyData".

There are totally 4 main data models that hold the data:
- **NorthBoundVehicle**: Data object that holds the readings of the vehicles that are moving in north direction. This object holds only hoseA readings of front and back axles.
- **SouthBoundVehicle**: Data object that holds the readings of the vehicles that are moving in south direction. This object holds both hoseA & hoseB readings of front and back axles.
- **EachDayData**: Data object that holds the vehicles data in both directions for each day.
- **SurveyData**: The root data object that holds EachDayData of all days from the given survey data. 

## Instructions for executing the code:
This program is developed using the Maven build tool. JUnit is the only third party library used for this program. So once you check out the code, build the code using `mvn clean install` command to ensure the program is builded properly by downloading all the required dependencies.

As per in the instructions document, the data file containing the full vehicle survey data is placed in [resources](https://github.com/SaiPradeepDandem/VehicleSurveyCodingChallenge/tree/master/src/main/resources/data) folder and when the program runs, it automatically picks the file from this location to parse and analyze the data.

To execute the program, run the main class **com.sai.vehiclesurvey.SurveyAnalyzer** from the IDE or command line. The analyzer program runs by picking the data file from resources, parses the data and prints the output in the console for different analyzing conditions/inputs.
For quick reference the output printed can be found [here](https://github.com/SaiPradeepDandem/VehicleSurveyCodingChallenge/tree/master/src/main/resources/output/output.txt).

## What is printed in the output?
When the program runs, the below analyis features are printed in the output console.

1. Total number of days for which the data is recorded.
2. For each day (i.e,.period of 24 Hours from 00:00 to 23:59) :
   - Total vehicles count going in north bound and sourth bound directions.
   - Total vehicles count going in each direction for morning (06:00 - 11:59) and evening (12:00 - 18:00) sessions.
   - Total vehicles count going in each direction for each period type in the day. 
3. Average vehicle count data in each direction for all days
   - For morning (06:00 - 11:59) and evening (12:00 - 18:00) sessions.
   - For each period type in the day.
4. For each day (i.e,.period of 24 Hours from 00:00 to 23:59), peak volume time(hour) in each direction for morning (06:00 - 11:59) and evening (12:00 - 18:00) sessions.<br/>
   *Sample data for peak volume time : `NB:17:00-18:00, SB:14:00-15:00`*
5. For each day (i.e,.period of 24 Hours from 00:00 to 23:59) :
   - Average vehicle's speed distribution going in each direction for morning (06:00 - 11:59) and evening (12:00 - 18:00) sessions.
   - Average vehicle's speed distribution going in each direction for each period type in the day. 
6. Average vehicle speed distribution data in each direction for all days
   - For morning (06:00 - 11:59) and evening (12:00 - 18:00) sessions.
   - For each period type in the day.
7. Average distance between vehicles (in meters) data in each direction for all days for each period type in the day.    

**_Note:_** *North bound vehicles data is prefixed with "NB:" and south bound vehicles data is prefixed with "SB:".*<br/>
      *The periods that are considered are **Per_Hour**, **Per_Half_Hour**, **Per_20_Minutes** & **Per_15_Minutes**.*
<br/><br/>The format of data for each period of time is as `<<period_start_time in HH:MM>> - NB:<<data>>, SB:<<data>>;`<br/>
     *Sample data for Per_Hour period: **00:00** - NB:7, SB:8;  **01:00** - NB:1, SB:7;  **02:00** - NB:2, SB:6; ...*<br/>
     *Sample data for Per_Half_Hour period: **00:00** - NB:3, SB:5;  **00:30** - NB:4, SB:3;  **01:00** - NB:0, SB:3; ...*<br/>
     *Sample data for Per_Hour Per_20_Minutes: **00:00** - NB:3, SB:2;  **00:20** - NB:2, SB:3;  **00:40** - NB:2, SB:3; ...*<br/>
     *Sample data for Per_HourPer_15_Minutesperiod: **00:00** - NB:2, SB:1;  **00:15** - NB:1, SB:4;  **00:30** - NB:3, SB:2; ...*<br/>
