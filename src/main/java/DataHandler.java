import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.values.FieldValue;
import oracle.nosql.driver.values.MapValue;

public class DataHandler {

    CloudActions databaseWorker;

    //
    // Description: Constructor for the DataHandler class
    // Parameters: 1. NoSQLHandle
    //
    public DataHandler(NoSQLHandle handle) {
        databaseWorker = new CloudActions(handle);
    }

    //
    // Description: Processes and uploads a .csv file to the cloud database
    // Parameters: 1. A file name or file path
    //             2. Letter which will be used for the primary key - Careful not to overwrite
    //             3. Course name corresponding to the .csv file
    //             4. Week - Integer for the week number
    //             5. Year
    //             6. Format of play (singles, doubles, etc.)
    //
    public void uploadCsvFileToDatabase(String fileName, String letter, String course, String week, String year, String format) {
        String csvLine = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            Integer count = 0;
            reader.readLine(); // This will skip the first line which contains header info

            while((csvLine = reader.readLine()) != null) {
                String[] playerData = csvLine.split(",");

                if(!playerData[1].contains("DNF")) { // Don't use the data if the player did not finish the round (DNF)
                    String[] player = preparePlayerDataForUpload(playerData, course, week, year, format);
                    uploadPlayerToDatabase(player, letter+count);
                    count++;
                }
            }
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    //
    // Description: Prepares the array of data that represents the player to be uploaded to the database
    // Parameters: 1. Array of player data that comes from a .csv file
    //             2. Course name corresponding to the .csv file
    //             3. Week - Integer for the week number
    //             4. Year
    //             5. Format of play (singles, doubles, etc.)
    //
    public String[] preparePlayerDataForUpload(String[] data, String course, String week, String year, String format) {
        String[] player = new String[8];

        String fullName = data[2];
        String cleanFullName = fullName.replaceAll("\"", "");
        String[] splitNames = cleanFullName.split(" ");

        player[0] = splitNames[0];
        if (splitNames.length > 1) // Some players will only have a first name
            player[1] = splitNames[1];
        else
            player[1] = "";
        player[2] = data[3];
        player[3] = data[4];
        player[4] = course;
        player[5] = week;
        player[6] = year;
        player[7] = format;

        return player;
    }

    //
    // Description: Uploads the player to the database
    // Parameters: 1. Array of all the player data
    //             2. String that will be used as a primary key for that row of data
    //
    public void uploadPlayerToDatabase(String[] player, String key) {
        int scoreRelativeToPar = 0;
        int scoreTotal = 0;
        int week = 0;
        int year = 0;

        try {
            scoreRelativeToPar = Integer.parseInt(player[2]);
            scoreTotal = Integer.parseInt(player[3]);
            week = Integer.parseInt(player[5]);
            year = Integer.parseInt(player[6]);

        }  catch (NumberFormatException e) {
            e.printStackTrace();
        }

        databaseWorker.addRow(key, player[0], player[1], scoreRelativeToPar, scoreTotal, player[4], week, year);

    }

    //
    // Description: Writes the query results to the console
    // Parameters: 1. ArrayList<MapValue> Data type returned by a query to the Oracle NoSQL database
    //             2. Results Description will describe the data that was queried
    //
    public void writeQueryToConsole(ArrayList<MapValue> results, String resultsDescription) {
        System.out.print(resultsDescription);
        for (MapValue res : results) {
            System.out.println();
            Map<String, FieldValue> map = res.getMap();
            map.forEach((key, value)-> System.out.print(value+","));
        }
    }

    //
    // Description: Allows writeQueryToConsole but without a results description parameter
    // Parameters: 1. ArrayList<MapValue> Data type returned by a query to the Oracle NoSQL database
    //
    public void writeQueryToConsole(ArrayList<MapValue> results) {
        writeQueryToConsole(results, "Query Results: ");
    }

    //
    // Description: Writes the query results to a .csv file in csv format
    // Parameters: 1. ArrayList<MapValue> Data type returned by a query to the Oracle NoSQL database
    //             2. Results Description will describe the data that was queried - Header row of .csv file
    //
    public void writeQueryToCsvFile(ArrayList<MapValue> results, String resultsDescription) {
        String fileName = "results.csv";

        try {
            File printFile = new File(fileName);
            if (printFile.createNewFile()) {
                System.out.println("New file created "+printFile.getName());
            } else {
                System.out.println("This file already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error creating a file!");
            e.printStackTrace();
        }

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(resultsDescription);
            for (MapValue res : results) {
                fileWriter.write("\n");
                Map<String, FieldValue> map = res.getMap();
                map.forEach((key, value)-> {
                    try {
                        fileWriter.write(value+",");
                    } catch (IOException e) {
                        System.out.println("An error occurred trying to write to the file!");
                        e.printStackTrace();
                    }
                });
            }
            fileWriter.close();
            System.out.println("Finished a successful write to the text file.");
        } catch (IOException e) {
            System.out.println("An error occurred trying to write to the file!");
            e.printStackTrace();
        }
    }


    //
    // Description: Allows writeQueryToCsvFile but without a results description parameter
    // Parameters: 1. ArrayList<MapValue> Data type returned by a query to the Oracle NoSQL database
    //
    public void writeQueryToCSVFile(ArrayList<MapValue> results) {
        writeQueryToCsvFile(results, "Query Result: ");
    }

    //
    // Description: Currently unused, but can be used to quickly upload a lot of files
    //      Be careful that the primary key (letter) will not be overriding previous entries - Check for this
    // Parameters: 1. List of file names (file paths) in string format
    //
    public void uploadListOfCsvFiles(String[] fileNames) {
        for(int i = 0; i<fileNames.length; i++) {
            uploadCsvFileToDatabase(fileNames[i], "a"+i, "Course Name", String.valueOf(i+1), "Year", "Format");
        }
    }
}
