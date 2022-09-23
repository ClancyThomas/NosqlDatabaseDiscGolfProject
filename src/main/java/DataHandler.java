import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import oracle.nosql.driver.NoSQLHandle;

public class DataHandler {

    CloudActions databaseWorker;

    public DataHandler(NoSQLHandle handle) {
        databaseWorker = new CloudActions(handle);
    }

    public void uploadCsvFileToDatabase(String fileName, Integer primaryKey, String week, String course) {
        String csvLine = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("LeagueData2022/" + fileName));
            Integer count = primaryKey;
            reader.readLine(); // This will skip the first line which contains header info

            while((csvLine = reader.readLine()) != null) {
                String[] playerData = csvLine.split(",");

                if(!playerData[1].contains("DNF")) { // Don't use the data if the player did not finish the round (DNF)
                    String[] player = prepareDataForUpload(playerData, week, course);
                    uploadToDatabase(player, count);
                    count++;
                }
            }
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public String[] prepareDataForUpload(String[] data, String week, String course) {
        String[] player = new String[6];

        String fullName = data[2];
        String cleanFullName = fullName.replaceAll("\"", "");
        String[] splitNames = cleanFullName.split(" ");

        player[0] = splitNames[0];
        if (splitNames.length > 1) // Some players will only have a first name
            player[1] = splitNames[1];
        else
            player[1] = "";
        player[2] = week;
        player[3] = course;
        player[4] = data[3];
        player[5] = data[4];

        return player;
    }

    public void uploadToDatabase(String[] player, Integer key) {
        int scoreRelativeToPar = 0;
        int scoreTotal = 0;
        int week = 0;

        try {
            week = Integer.parseInt(player[2]);
            scoreRelativeToPar = Integer.parseInt(player[4]);
            scoreTotal = Integer.parseInt(player[5]);
        }  catch (NumberFormatException e) {
            e.printStackTrace();
        }

        databaseWorker.addRow(key, player[0], player[1], week, player[3], scoreRelativeToPar, scoreTotal);

    }
}
