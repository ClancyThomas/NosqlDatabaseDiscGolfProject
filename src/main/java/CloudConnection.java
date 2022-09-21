import java.io.IOException;

import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.NoSQLHandleConfig;
import oracle.nosql.driver.NoSQLHandleFactory;
import oracle.nosql.driver.iam.SignatureProvider;

public class CloudConnection {

    /* Information used for connecting to the database and a specific compartment */
    private String endpoint = "us-phoenix-1";
    private String compartment = "practice";

    public NoSQLHandle getHandle() {
        NoSQLHandleConfig config = new NoSQLHandleConfig(endpoint);

        if (compartment != null) {
            config.setDefaultCompartment(compartment);
        }

        config.setRequestTimeout(5000);
        configureAuth(config);
        NoSQLHandle handle = NoSQLHandleFactory.createNoSQLHandle(config);
        return handle;
    }

    private void configureAuth(NoSQLHandleConfig config) {
        try {
            SignatureProvider authProvider = new SignatureProvider(); // Arguments for this come from file in $HOME/.oci/config file
            config.setAuthorizationProvider(authProvider);
        } catch (IOException ioe) {
            System.err.println("Error attempting to configure authentication: " +
                    ioe);
            System.exit(1);
        }
    }


}
