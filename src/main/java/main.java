import oracle.nosql.driver.NoSQLHandle;

public class Main {
    public static void main(String[] args) {
        CloudConnection cloudConnection = new CloudConnection();

        try (NoSQLHandle handle = cloudConnection.getHandle()) {

        }
    }
}

