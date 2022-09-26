import java.io.IOException;

import oracle.nosql.driver.NoSQLHandle;
import oracle.nosql.driver.NoSQLHandleConfig;
import oracle.nosql.driver.NoSQLHandleFactory;
import oracle.nosql.driver.iam.SignatureProvider;

public class CloudConnection {

    // Information used for connecting to the database and a specific compartment
    private String endpoint = "us-phoenix-1"; // [INSERT] Add your region here
    private String compartment = "practice";  // [INSERT] Specify which compartment to access

    //
    // Description: Gets the handle for the cloud database which is used for the interacting with database
    //
    public NoSQLHandle getHandle() {

        NoSQLHandleConfig config = new NoSQLHandleConfig(endpoint);
        config.setDefaultCompartment(compartment);
        config.setRequestTimeout(10000);
        configureAuth(config);
        NoSQLHandle handle = NoSQLHandleFactory.createNoSQLHandle(config);
        return handle;

    }

    //
    // Description: Configures the authorization for the cloud database
    // Parameters: 1. NoSQLHandleConfig - Created as part of the connection process
    //
    private void configureAuth(NoSQLHandleConfig config) {
        try {
            //
            // Arguments for this come from file in $HOME/.oci/config file
            // [INSERT] Configure/Create this file on your machine
            // More Info: https://docs.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm
            //
            SignatureProvider authProvider = new SignatureProvider();
            config.setAuthorizationProvider(authProvider);
        } catch (IOException ioe) {
            System.err.println("Error attempting to configure authentication: " +
                    ioe);
            System.exit(1);
        }
    }


}
