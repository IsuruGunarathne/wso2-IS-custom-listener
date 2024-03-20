package org.wso2.custom.user.operation.event.listener;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.*;

import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import io.github.cdimascio.dotenv.Dotenv;
/**
 *
 */
public class CustomUserOperationEventListener extends AbstractUserOperationEventListener {

    private String systemUserPrefix = "system_";

    // database connector

    public static CqlSession connectToCassandra(Dotenv dotenv) {

        // Load environment variables from .env file
        String cassandraHost = dotenv.get("COSMOS_CONTACT_POINT");
        int cassandraPort = Integer.parseInt(dotenv.get("COSMOS_PORT"));

        String cassandraUsername = dotenv.get("COSMOS_USER_NAME");
        String cassandraPassword = dotenv.get("COSMOS_PASSWORD");   
        String region = dotenv.get("COSMOS_REGION");     

        SSLContext sc = null;
        try{

            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(null, null);

            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);

            sc = SSLContext.getInstance("TLSv1.2");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        catch (Exception e) {
            System.out.println("Error creating keystore");
            e.printStackTrace();
        } 

        CqlSession session = CqlSession.builder().withSslContext(sc)
        .addContactPoint(new InetSocketAddress(cassandraHost, cassandraPort)).withLocalDatacenter(region)
        .withAuthCredentials(cassandraUsername, cassandraPassword).build();

        System.out.println("Creating session: " + session.getName());
        return session;
    }

    public static void close(CqlSession session) {
        session.close();
    }

    public static void test(String userName,Map<String, String>  claims) {
        // Cassandra connection parameters
        Dotenv dotenv = Dotenv.load();

        String keyspace = dotenv.get("CASSANDRA_KEYSPACE");
        String table = dotenv.get("CASSANDRA_TABLE");        
        
        System.out.println("Connecting to Cassandra...");
        // Establishing connection to Cassandra

        try (CqlSession session = connectToCassandra(dotenv)) {
            System.out.println("Connected to Cassandra.");
            // Writing data to the user_data table
            String query = String.format("INSERT INTO %s.%s (user_id, user_name) VALUES ('%s','%s');", keyspace, table, claims.get("http://wso2.org/claims/userid"),userName);
            session.execute(query);

            System.out.println("Data written to user_data table successfully.");
        } catch (Exception e) {
            System.err.println("Error: " + e);
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
                test(userName,claims);

        return true;
    }
}

