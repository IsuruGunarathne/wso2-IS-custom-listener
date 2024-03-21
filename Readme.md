## Adding A custom listener to WSO2 IS

#### Copy a copy of the IS to the root directory

The folder should be named `wso2is-7.0.0`

##### Command to Build

`JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64 mvn clean install`

#### Run the following script

This will automatically build the required jar and copy the jar to the correct location in the IS and start the IS

`./build_add_run_is.sh`

### env

copy the .env file to wso2is-7.0.0 directory, this file has the credentials for the cassandra connection (cosmos_db)
