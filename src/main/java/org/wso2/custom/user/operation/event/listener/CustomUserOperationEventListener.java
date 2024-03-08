package org.wso2.custom.user.operation.event.listener;
import java.util.Map;

import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.*;


/**
 *
 */
public class CustomUserOperationEventListener extends AbstractUserOperationEventListener {

    private String systemUserPrefix = "system_";


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
                System.out.println("Role List: " + roleList);
                System.out.println("Claims: " + claims);
                System.out.println("Profile: " + profile);
                System.out.println("");

        return true;
    }
}
