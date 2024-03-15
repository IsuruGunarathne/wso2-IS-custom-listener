package org.wso2.custom.user.operation.event.listener;
import java.util.Map;

import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.*;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
/**
 *
 */
public class CustomUserOperationEventListener extends AbstractUserOperationEventListener {

    private String systemUserPrefix = "system_";

    // database connector

    private static final String KEYSPACE_NAME = "sync";
    private static final String CONTACT_POINT = "127.0.0.1";

    public static CqlSession connect() {
        return CqlSession.builder()
                .withKeyspace(KEYSPACE_NAME)
                .addContactPoint(new InetSocketAddress(CONTACT_POINT, 9042))
                .build();
    }

    public static void close(CqlSession session) {
        session.close();
    }

    public static void test() {
        // Cassandra connection parameters
        String contactPoint = "127.0.0.1"; // Change this to your Cassandra node's IP
        int port = 9042; // Default Cassandra port
        String keyspace = "sync"; // Keyspace name
        String table = "user_data"; // Table name

        System.out.println("Connecting to Cassandra...");
        // Establishing connection to Cassandra
        try (CqlSession session = new CqlSessionBuilder()
                .addContactPoint(new InetSocketAddress(contactPoint, port))
                .withLocalDatacenter("datacenter1") // Adjust to your local datacenter name
                .build()) {

            // Writing data to the user_data table
            String query = String.format("INSERT INTO %s.%s (user_id, user_name) VALUES ('tom','dick');", keyspace, table);
            session.execute(query);

            System.out.println("Data written to user_data table successfully.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public CustomUserOperationEventListener() {
        super();
    }


    @Override
    public int getExecutionOrderId() {
        return 9000;
    }

    @Override
    public boolean doPreDeleteUser
            (String s, UserStoreManager userStoreManager) throws UserStoreException {
                System.out.println("doPreDeleteUser");
                System.out.println("Yaaaaaaaaay!!!!!!!!!!!");
        if (s.contains(systemUserPrefix)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean doPreDeleteUserWithID
            (String s, UserStoreManager userStoreManager) throws UserStoreException {
            System.out.println("doPreDeleteUserWithID");
        if (s.contains(systemUserPrefix)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean doPostAddUser
            (String userName, Object credential, String[] roleList, Map<String, String> claims,
            String profile, UserStoreManager userStoreManager) throws UserStoreException {
                System.out.println("doPostAddUser");
                System.out.println("");
                System.out.println("A user was added to the system");
                System.out.println("");
                System.out.println("User Name: " + userName);
                System.out.println("Credential: " + credential);
                System.out.println("");
                System.out.println("");
                System.out.println("Role List: " + roleList);
                System.out.println("");
                System.out.println("");
                System.out.println("Profile; ");
                System.out.println(profile);
                System.out.println("");
                System.out.println("claims");
                // print the claims line by line
                for (Map.Entry<String, String> entry : claims.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                System.out.println("");
                System.out.println("");
                test();

        return true;
    }
}
