import java.io.IOException;

import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.NoSQLHandleConfig;
import oracle.nosql.driver.NoSQLHandleFactory;
import oracle.nosql.driver.iam.SignatureProvider;

import java.io.IOException;

public class CloudConnection {

    private String endpoint = "us-phoenix-1"; // Replace with your region
    private String compartment = "practice";  // Replace with your compartment name

    public NoSQLHandle getHandle() {
        NoSQLHandleConfig config = new NoSQLHandleConfig(endpoint);
        config.setDefaultCompartment(compartment);
        config.setRequestTimeout(10000);
        configureAuth(config);
        return NoSQLHandleFactory.createNoSQLHandle(config);
    }

    private void configureAuth(NoSQLHandleConfig config) {
        try {
            SignatureProvider authProvider = new SignatureProvider();
            config.setAuthorizationProvider(authProvider);
        } catch (IOException e) {
            System.err.println("Error configuring authentication: " + e.getMessage());
            System.exit(1);
        }
    }
}

