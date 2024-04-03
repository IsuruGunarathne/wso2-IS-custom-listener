package org.wso2.custom.user.operation.event.listener;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.*;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

import java.net.InetSocketAddress;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
/**
 *
 */
public class CustomUserOperationEventListener extends AbstractUserOperationEventListener {

    private String systemUserPrefix = "system_";
    private static final String INSERT_QUERY = "INSERT INTO sync.users (user_id, username, credential, role_list, claims, profile, central_us, east_us) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static CqlSession session;

    // database connector
    public static void createKeyspace(CqlSession session, String keyspace){
        // Create a keyspace
        String query = "CREATE KEYSPACE IF NOT EXISTS " + keyspace + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 2};";
        session.execute(query);
    }

    public static void createTable(CqlSession session, String keyspace, String table){
        // Create a table
        
        String query = "CREATE TABLE IF NOT EXISTS " + keyspace + "." + table + " (\n" + //
                        "  central_us BOOLEAN,\n" + //
                        "  east_us BOOLEAN,\n" + //
                        "  user_id TEXT,\n" + //
                        "  username TEXT,\n" + //
                        "  credential TEXT,\n" + //
                        "  role_list SET<TEXT>,\n" + //
                        "  claims MAP<TEXT, TEXT>,\n" + //
                        "  profile TEXT,\n" + //
                        "  PRIMARY KEY ((central_us, east_us), user_id)\n" + //
                        ");";
        session.execute(query);
    }

    public static CqlSession connectToCassandra(Dotenv dotenv) {

        System.out.println("Connecting to Cassandra.......");
        // Load environment variables from .env file
        String cassandraHost = dotenv.get("COSMOS_CONTACT_POINT");
        int cassandraPort = Integer.parseInt(dotenv.get("COSMOS_PORT"));

        String cassandraUsername = dotenv.get("COSMOS_USER_NAME");
        String cassandraPassword = dotenv.get("COSMOS_PASSWORD");   
        String region = dotenv.get("COSMOS_REGION");    
        String ref_path = dotenv.get("COSMOS_REF_PATH");

        
        // put the absolute path to the reference.conf file here
        File file = new File(ref_path);
        // print the content of the file
        System.out.println("File path: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        DriverConfigLoader loader = DriverConfigLoader.fromFile(file);

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
        .withConfigLoader(loader)   
        .withAuthCredentials(cassandraUsername, cassandraPassword).build();

        System.out.println("Creating session: " + session.getName());
        return session;
    }

    public static void close(CqlSession session) {
        session.close();
    }

    public static void writeToCassandra(String userName, Object credential, String[] roleList, Map<String, String> claims,
    String profile) {

        try {
            // Writing data to the user_data table
            String userId = claims.get("http://wso2.org/claims/userid");
            Set<String> roleSet = new HashSet<>(Arrays.asList(roleList));
            
            PreparedStatement preparedStatement = session.prepare(INSERT_QUERY);
            BoundStatement boundStatement = preparedStatement.bind(
                userId,                // user_id
                userName,             // username
                credential.toString(),// credential
                roleSet,              // role_list
                claims,               // claims
                profile,                // profile
                true,               // central_us
                false);             // east_us
            session.execute(boundStatement);

            System.out.println("Data written to user_data table successfully.");
            // close(session);
            // System.out.println("Connection to Cassandra closed.");
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    public CustomUserOperationEventListener() {
        super();
        Dotenv dotenv = Dotenv.load();
        String keyspace = dotenv.get("CASSANDRA_KEYSPACE");
        String table = dotenv.get("CASSANDRA_TABLE");   

        session = connectToCassandra(dotenv);
        System.out.println("Connected to Cassandra.");
        createKeyspace(session, keyspace);
        System.out.println("Keyspace created");
        createTable(session, keyspace, table);
        System.out.println("Table created");
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
                System.out.println("Writing to Cassandra");
                writeToCassandra(userName, credential, roleList, claims, profile);

        return true;
    }

    @Override
    public boolean doPostUpdateUserListOfRoleWithID(String roleName, String[] deletedUsers, String[] newUsers,
            UserStoreManager userStoreManager) throws UserStoreException {
                System.out.println("doPostUpdateUserListOfRoleWithID");
                System.out.println("Role Name: " + roleName);
                System.out.println("Deleted Users: ");
                for (String userId : deletedUsers) {
                    System.out.println(userId);
                }
                System.out.println("New Users: ");
                for (String userId : newUsers) {
                    System.out.println(userId);
                }
        return true;
    }
}

