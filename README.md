# Lyon_VDF

This repository provides a structured setup for configuring a pom.xml file and integrating a VDF (Volume-Delay Function) module. It enables the generation of a .jar file based on a custom lyon_config, allowing users to run MATSim simulations with VDF support tailored to the Lyon metropolitan area.

Run the command : mvn clean package -Pstandalone

It will create a target folder where inside you will find two .jar file 

Copy those files to your simulation MATSim folder in output and run the following command : 

java -Xmx120G -jar Lyon_VDF-1.0-SNAPSHOT-standalone.jar --config-path lyon_config.xml

It will run the MATSim simulation with 120G RAM based on yout lyon_config file using the .jar you just created before