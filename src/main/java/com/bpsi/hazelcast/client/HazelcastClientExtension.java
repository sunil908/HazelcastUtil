package com.bpsi.hazelcast.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.json.JSONException;
import org.json.JSONObject;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastClientExtension {

	public static HazelcastInstance createHazelClient() {
		return HazelcastClient.newHazelcastClient();
	}
	
	// Create a client instance using a json file
	public static HazelcastInstance createHazelClient(String jsonFilename) throws IOException, JSONException {
		return createHazelClient(parseJSONFile(jsonFilename));
	}
	
	// Create a client instance using a json object
	public static HazelcastInstance createHazelClient(JSONObject jsonConfig){
		ClientConfig config = new ClientConfig();
        
		System.out.println("Entering the json config file....");
        
        String clusterpassword = jsonConfig.getString("clusterpassword");
        String clustername = jsonConfig.getString("clustername");
        String clustertoken = jsonConfig.getString("clustertoken");
        String discoveryurl = jsonConfig.getString("discoveryurl");
        
        System.out.println("clustername: "+clustername);
        System.out.println("clusterpassword: "+clusterpassword);
        System.out.println("clustertoken: " + clustertoken);
        System.out.println("discoveryurl: "+ discoveryurl);
        
		config.setGroupConfig(new GroupConfig(jsonConfig.getString("clustername"),jsonConfig.getString("clusterpassword")));
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), jsonConfig.getString("clustertoken"));
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), jsonConfig.getString("discoveryurl"));
        
        return HazelcastClient.newHazelcastClient(config);
	}
	
	
	public static HazelcastInstance createHazelClient(String clusterName, String clusterPassword, String clusterToken, String DiscoveryURL) {
		ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(clusterName,clusterPassword));
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), clusterToken);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), DiscoveryURL);
        
        return HazelcastClient.newHazelcastClient(config);
	}
	
	// Create a client instance using a json object
	public static String getTargetMap(String jsonFilename) throws IOException, JSONException {
		JSONObject jsonConfig = parseJSONFile(jsonFilename);
		System.out.println("Entering the json config file....");
        String targetmap = jsonConfig.getString("targetmap");
        System.out.println("targetmap: "+targetmap);
        return targetmap;
	}

	
	public static JSONObject parseJSONFile(String filename)  throws IOException, JSONException{
        //System.out.println("Data...\n" + content );

		try { String content = null; 
				try {
		    		Path path = Paths.get(filename);
		            content = new String(Files.readAllBytes(path));
		        }
		        catch (IOException e) {
		        	System.out.println("Error: Unable to open file. Check your file and location is correct.");
		        }
				return new JSONObject(content);
		}
        catch (JSONException e) {
        	System.out.println("Error: Unable to open JSON file. Check your configurations.");
        }
		return null;
    }
}
