import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.values.FieldValue;
import oracle.nosql.driver.values.MapValue;

public class DataHandler {

    private final CloudActions databaseWorker;

    public DataHandler(NoSQLHandle handle, String tableName) {
        databaseWorker = new CloudActions(handle, tableName);
    }

    public CloudActions getCloudActions() {
        return databaseWorker;
    }

    public void uploadCsvFileToDatabase(String fileName, String letter, String course, String week, String year, String format) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            Integer count = 0;
            reader.readLine(); // Skip the header line

            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] playerData = csvLine.split(",");
                if (!playerData[1].contains("DNF")) {
                    String[] player = preparePlayerDataForUpload(playerData, course, week, year, format);
                    uploadPlayerToDatabase(player, letter + count);
                    count++;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String[] preparePlayerDataForUpload(String[] data, String course, String week, String year, String format) {
        String[] player = new String[8];

        String fullName = data[2];
        String cleanFullName = fullName.replaceAll("\"", "");
        String[] splitNames = cleanFullName.split(" ");

        player[0] = splitNames[0];
        player[1] = (splitNames.length > 1) ? splitNames[1] : "";
        player[2] = data[3];
        player[3] = data[4];
        player[4] = course;
        player[5] = week;
        player[6] = year;
        player[7] = format;

        return player;
    }

    public void uploadPlayerToDatabase(String[] player, String key) {
        try {
            int scoreRelativeToPar = Integer.parseInt(player[2]);
            int scoreTotal = Integer.parseInt(player[3]);
            int week = Integer.parseInt(player[5]);
            int year = Integer.parseInt(player[6]);

            databaseWorker.addRow(key, player[0], player[1], scoreRelativeToPar, scoreTotal, player[4], week, year, player[7]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in player data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error uploading player to database: " + e.getMessage());
        }
    }

    public void writeQueryToConsole(ArrayList<MapValue> results, String resultsDescription) {
        System.out.println("\n\n-------------------------------------------------");
        System.out.print(resultsDescription);
        for (MapValue res : results) {
            System.out.println();
            Map<String, FieldValue> map = res.getMap();
            map.forEach((key, value) -> System.out.print(value + ","));
        }
        System.out.println("\n-------------------------------------------------");
    }

    public void writeQueryResults(ArrayList<MapValue> results, String resultsDescription, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(resultsDescription + "\n");
            for (MapValue res : results) {
                Map<String, FieldValue> map = res.getMap();
                map.forEach((key, value) -> {
                    try {
                        fileWriter.write(value + ",");
                    } catch (IOException e) {
                        System.err.println("Error writing to file: " + e.getMessage());
                    }
                });
                fileWriter.write("\n");
            }
            System.out.println("Finished writing to " + fileName);
        } catch (IOException e) {
            System.err.println("Error creating or writing to the file: " + e.getMessage());
        }
    }
}
