jarFile= org.wso2.custom.user.operation.event.listener-1.0-SNAPSHOT.jar
target_path= target
destination= wso2is-7.0.0/repository/components/dropins

JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64 mvn clean install

# if the jar already exists at destination, remove it
if [ -f $destination/$jarFile ]; then
    rm $destination/$jarFile
fi

# copy jar from target to the product
cp $target_path/$jarFile $destination

# restart the server
./wso2is-7.0.0/bin/wso2server.sh


