# Delete the existing wso2is-7.0.0 folder
rm -rf wso2is-7.0.0

# Extract the ZIP file
# rc7
unzip wso2is-7.0.0.zip

# Copy the wso2is-7.0.0 folder in the  extracted folder to the destination directory

# rc7
# cp -r wso2is-7.0.0-rc7/wso2is-7.0.0 .

# Remove the extracted folder
# rc7
# rm -rf wso2is-7.0.0

echo "Extraction and copying completed."

# build the project

JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64 mvn clean install

# copy to the dropins folder
rm -rf wso2is-7.0.0/repository/components/dropins/org.wso2.custom.user.operation.event.listener-1.0-SNAPSHOT.jar
cp target/org.wso2.custom.user.operation.event.listener-1.0-SNAPSHOT.jar wso2is-7.0.0/repository/components/dropins


# copy libraries from libraries directory to the lib folder
cp libraries/* wso2is-7.0.0/repository/components/lib

# copy deployment.toml to the conf folder
cp deployment/deployment.toml wso2is-7.0.0/repository/conf

# copy .env to wso2is-7.0.0
cp .env wso2is-7.0.0

# restart the server
echo "Restarting the server"
./wso2is-7.0.0/bin/wso2server.sh


