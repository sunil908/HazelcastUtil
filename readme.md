# USAGE :

Download the git repository and unzip the build package. Browse into the target folder for the jar file to be used for running the utility. 
We might want to wrap this into a bat command file as enhancement in future.

Command Line: 

java -jar HazelcastUtil-final.jar -setConfig  hazelcastConfig.json -destroyAllMaps

Parameters 

-setConfig : Refers to JSON filename containing below cluster details

                         {"clustername":"MY CLUSTER NAME",
                          "clusterpassword":"MY CLUSTER PASSWORD",
                          "clustertoken":"MY CLUSTER TOKEN",
                          "discoveryurl":"MY DISCOVERY URL"
                         }
                         
-destroyAllMaps : Destroys all the maps in Hazelcast cluster
-clearAllMaps : Clears cache all the maps in Hazelcast cluster

Notes:

A sample json file for configuration is available to specify the cluster details in /target folder. You may reuse the same.
The only command supported right now is 

# -setConfig

Expects a json file with specified hazelcast cluster configuration. This is a optional parameter, meaning it will locate a local cluster 
(localhost) if nothing specified. You may also give local path to the json config. Format is given above.

# -destroyAllMaps :

It destroys all the maps that is present in the hazelcast cluster. There might be issues when someone is writing data to the maps 
and it is locked. Currently it is a crude operation and requires safety and enchancements.

# -clearAllMaps :

It clears the maps of all the contents instead of destroying them by dropping the maps. Therefore it will empty all the key value pairs.



