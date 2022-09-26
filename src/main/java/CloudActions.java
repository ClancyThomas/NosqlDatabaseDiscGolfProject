import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ops.PutRequest;
import oracle.nosql.driver.ops.PutResult;
import oracle.nosql.driver.ops.QueryRequest;
import oracle.nosql.driver.ops.QueryResult;
import oracle.nosql.driver.values.MapValue;

import java.util.ArrayList;

public class CloudActions {

    NoSQLHandle myHandle;
    private String tableName = "TetonRiverWeeklyStats"; // [INSERT] Specify table name here

    //
    // Description: Constructor for the CloudActions class
    // Parameters: 1. NoSQLHandle
    //
    public CloudActions(NoSQLHandle handle) {
        myHandle = handle;
    }

    //
    // Description: Adds a row to the cloud database through a 'put' request - Format corresponds to the table chosen
    // Parameters: 1. Primary Key
    //             2. First name of the player
    //             3. Last name of the player
    //             4. Score Relative to Par
    //             5. Total Score
    //             6. Course
    //             7. Week - Integer for the week number
    //             8. Year
    //
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

    //
    // Description: Queries against the cloud database using a query request
    // Parameters: 1. A string that contains a query statement in Oracle NoSQL format
    //
    public ArrayList<MapValue> query(String sqlQuery) {
        QueryRequest queryRequest = new QueryRequest()
                .setStatement(sqlQuery);
        ArrayList<MapValue> results = new ArrayList<MapValue>();

        // Use a do-while loop because the results may not all come back at once on each query
        do {
            QueryResult queryResult = myHandle.query(queryRequest);
            results.addAll(queryResult.getResults());
        } while (!queryRequest.isDone());

        return results;
    }


}
