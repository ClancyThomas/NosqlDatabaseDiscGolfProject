import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ops.PutRequest;
import oracle.nosql.driver.ops.PutResult;
import oracle.nosql.driver.ops.QueryRequest;
import oracle.nosql.driver.ops.QueryResult;
import oracle.nosql.driver.values.MapValue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CloudActions {

    NoSQLHandle myHandle;
    private String tableName = "TetonRiverWeeklyStats"; // [INSERT] Specify table name here

    public CloudActions(NoSQLHandle handle) {
        myHandle = handle;
    }

    public void addRow(String key, String firstName, String lastName, Integer scoreRelativeToPar,
                       Integer scoreTotal, String course, Integer week, Integer year) {
        MapValue row = new MapValue ()
                .put("id", key)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("scoreRelativeToPar", scoreRelativeToPar)
                .put("scoreTotal", scoreTotal)
                .put("course", course)
                .put("week", week)
                .put("year", year)

                ;

        PutRequest putRequest = new PutRequest()
                .setValue(row)
                .setTableName(tableName);

        PutResult putRes = myHandle.put(putRequest);

        System.out.println("Added row. Result: " + putRes);
    }

    public void query(String sqlQuery, Boolean printToTextFile) {
        QueryRequest queryRequest = new QueryRequest()
                .setStatement(sqlQuery);
        ArrayList<MapValue> results = new ArrayList<MapValue>();

        // Use a do-while loop because the results may not all come back at once on each query
        do {
            QueryResult queryResult = myHandle.query(queryRequest);
            results.addAll(queryResult.getResults());
        } while (!queryRequest.isDone());
        if (printToTextFile) {
            printQueryToTextFile(results);
        } else {
            printQueryToConsole(results);
        }

    }

    public void printQueryToConsole(ArrayList<MapValue> results) {
        System.out.println("Query Result:");
        for (MapValue res : results) {
            System.out.println("\t" + res);
        }
    }

    public void printQueryToTextFile(ArrayList<MapValue> results) {
        String fileName = "results.txt";

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
            fileWriter.write("Query Result: ");
            for (MapValue res : results) {
                fileWriter.write("\n" + res);
            }
            fileWriter.close();
            System.out.println("Finished a successful write to the text file.");
        } catch (IOException e) {
            System.out.println("An error occurred trying to write to the file!");
            e.printStackTrace();
        }
    }
}
