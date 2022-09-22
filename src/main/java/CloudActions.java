import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ops.PutRequest;
import oracle.nosql.driver.ops.PutResult;
import oracle.nosql.driver.values.MapValue;

public class CloudActions {

    NoSQLHandle myHandle;
    private String tableName = "TetonRiverDGC"; // [INSERT] Specify table name here

    public CloudActions(NoSQLHandle handle) {
        myHandle = handle;
    }

    public void addRow(Integer key, String firstName, String lastName, Integer week,
                       String course, Integer scoreRelativeToPar, Integer scoreTotal) {
        MapValue row = new MapValue ()
                .put("Key", key)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("week", week)
                .put("course", course)
                .put("scoreRelativeToPar", scoreRelativeToPar)
                .put("scoreTotal", scoreTotal);

        PutRequest putRequest = new PutRequest()
                .setValue(row)
                .setTableName(tableName);

        PutResult putRes = myHandle.put(putRequest);

        System.out.println("Put row, result " + putRes);
    }
}
