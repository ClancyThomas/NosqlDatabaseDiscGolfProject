import oracle.nosql.driver.NoSQLHandle;

public class Main {

    public static void main(String[] args) {
        CloudConnection cloudConnection = new CloudConnection();

        try (NoSQLHandle handle = cloudConnection.getHandle()) {
            DataHandler dataHandler = new DataHandler(handle);
            dataHandler.uploadCsvFileToDatabase("teton_20220329.csv", 0, "1", "Nature Park");

        }

    }
}

