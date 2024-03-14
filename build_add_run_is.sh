jarFile= org.wso2.custom.user.operation.event.listener-1.0-SNAPSHOT.jar
target_path= target
destination= wso2is-7.0.0/repository/components/dropins

# build the project

JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64 mvn clean install

# copy to the dropins folder
rm -rf wso2is-7.0.0/repository/components/dropins/org.wso2.custom.user.operation.event.listener_1.0_SNAPSHOT_1.0.0.jar
cp target/org.wso2.custom.user.operation.event.listener-1.0-SNAPSHOT.jar wso2is-7.0.0/repository/components/lib

# restart the server
echo "Restarting the server"
./wso2is-7.0.0/bin/wso2server.sh -DosgiConsole


