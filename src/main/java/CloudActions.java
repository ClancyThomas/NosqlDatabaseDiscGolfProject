import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.ops.PutRequest;
import oracle.nosql.driver.ops.QueryRequest;
import oracle.nosql.driver.ops.QueryResult;
import oracle.nosql.driver.values.MapValue;

import java.util.ArrayList;

public class CloudActions {

    private final NoSQLHandle handle;
    private final String tableName;

    public CloudActions(NoSQLHandle handle, String tableName) {
        this.handle = handle;
        this.tableName = tableName;
    }

    public void addRow(String key, String firstName, String lastName, Integer scoreRelativeToPar,
                       Integer scoreTotal, String course, Integer week, Integer year, String format) {
        MapValue row = new MapValue()
                .put("id", key)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("scoreRelativeToPar", scoreRelativeToPar)
                .put("scoreTotal", scoreTotal)
                .put("course", course)
                .put("week", week)
                .put("year", year)
                .put("format", format);

        PutRequest request = new PutRequest().setValue(row).setTableName(tableName);
        handle.put(request);
    }

    public ArrayList<MapValue> query(String sqlQuery) {
        QueryRequest queryRequest = new QueryRequest().setStatement(sqlQuery);
        ArrayList<MapValue> results = new ArrayList<>();

        do {
            QueryResult queryResult = handle.query(queryRequest);
            results.addAll(queryResult.getResults());
        } while (!queryRequest.isDone());

        return results;
    }
}
