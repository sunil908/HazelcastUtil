package com.bpsi.hazelcast.util;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class HazelcastMapUtil {
	
	static HazelcastInstance hazelClientInstance;
	
	public static void setHazelInstance(HazelcastInstance hazelcastInstance) {
		hazelClientInstance = hazelcastInstance;
	}
	
	public static void setHazelInstance() {
		hazelClientInstance = HazelcastClient.newHazelcastClient();
	}
	
	
	public static void setHazelInstance(JSONObject jsonConfig) {
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
        
        hazelClientInstance = HazelcastClient.newHazelcastClient(config);

	}
	
	
	public static void setHazelInstance(String clusterName, String clusterPassword, String clusterToken, String DiscoveryURL) {
		ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(clusterName,clusterPassword));
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), clusterToken);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), DiscoveryURL);
        
        hazelClientInstance = HazelcastClient.newHazelcastClient(config);

	}
	
	public static void clearAllMap() {
		
		IMap<Object,Object> map = null;
		try {
	        Collection<DistributedObject> distributedObjects = hazelClientInstance.getDistributedObjects();
	        if (distributedObjects.isEmpty()) {
	        	System.out.println("WARNING: No maps to clear");
	        }
	        else {
		        for (DistributedObject object : distributedObjects) {
		            if (object instanceof IMap) {
		                map = hazelClientInstance.getMap(object.getName());
		                System.out.println("WARNING: Cleared Mapname=" + map.getName());
		                map.clear();
		            }
		        }
	        }
		}
		catch(Exception e) {
			System.out.println("Err message:" + e.toString());
			System.out.println("Error: Unable to clear cache for the map : ");
		}
	}
	
	
	public static void destroyAllMap() {
		IMap<Object,Object> map = null;
		try {
	        Collection<DistributedObject> distributedObjects = hazelClientInstance.getDistributedObjects();
	        if (distributedObjects.isEmpty()) {
	        	System.out.println("WARNING: No maps to destroy");
	        }
	        else {
		        for (DistributedObject object : distributedObjects) {
		            if (object instanceof IMap) {
		                map = hazelClientInstance.getMap(object.getName());
		                System.out.println("WARNING: Deleted Mapname=" + map.getName());
		                map.destroy();
		            }
		        }	        	
	        }

		}
		catch(Exception e) {
			System.out.println("Err message:" + e.toString());
			System.out.println("ERROR: Unable to destroy the map ");
		}
	}
	
	public static void printUsage() {
		System.out.println("USAGE :");
		System.out.println("-setConfig : Refers to JSON filename containing below cluster details");
		System.out.println("			 {\"clustername\":\"MY CLUSTER NAME\",");
		System.out.println("			  \"clusterpassword\":\"MY CLUSTER PASSWORD\",");
		System.out.println("			  \"clustertoken\":\"MY CLUSTER TOKEN\",");
		System.out.println("			  \"discoveryurl\":\"MY DISCOVERY URL\"");
		System.out.println("			 }");
		System.out.println("-destroyAllMaps : Destroys all the maps in Hazelcast cluster");
		System.out.println("-clearAllMaps : Clears cache all the maps in Hazelcast cluster");
	}
	
	public static void exitCommandLine(int exitCode) {
		switch(exitCode)		{
		case 0: hazelClientInstance.shutdown(); 
				break;
		case -1:System.out.println("Error: No arg passed to Hazelcast Utility CLI 0.0.1");
				printUsage();
				System.exit(1);
				break;
		case -2:System.out.println("Error: Unable to find the valid arguments");
				printUsage();
				System.exit(1);
				break;
		case -3:System.out.println("Error: Unable to use Config settings");
				printUsage();
				System.exit(1);
				break;
		default: System.err.println("Error: Unknown command args");
		}
	}
	
	public static JSONObject parseJSONFile(String filename)  {
        //System.out.println("Data...\n" + content );

		try { String content = null; 
				try {
		    		Path path = Paths.get(filename);
		            content = new String(Files.readAllBytes(path));
		        }
		        catch (IOException e) {
		        	System.out.println("Error: Unable to open file. Check your file and location is correct.");
		        	exitCommandLine(-3);
		        }
				return new JSONObject(content);
		}
        catch (JSONException e) {
        	System.out.println("Error: Unable to open JSON file. Check your configurations.");
        	exitCommandLine(-3);
        }
		return null;
    }
	
    public static void main(String[] args) {
    		
    		
    		
    		List<String> list = Arrays.asList(args);
    		List<String> argList = new ArrayList<String>();
    		
    		argList.add("-destroyAllMaps");
    		argList.add("-clearAllMaps");
    		argList.add("-setConfig");
    		
    		int setConfigIndex = list.indexOf("-setConfig");
    		System.out.println("Set config index: "+setConfigIndex);
    		
    		/*
    		for(String argStr: list) {
    			boolean argFound = argList.stream()
	    				  .filter(argSearch -> argStr.equals(argSearch.toString()))
	    				  .findAny()
	    				  .isPresent();
    			if(!argFound) {
    				exitCommandLine(-2);
    			}
    		} */
    		
    		
    				
    		if (list.size()==0) {
    			exitCommandLine(-1);
    		}

    		if (setConfigIndex != -1 ) {
    			setHazelInstance(parseJSONFile(list.get(setConfigIndex+1)));
    		}
    		else {
    			HazelcastMapUtil.setHazelInstance();
    		}

    		if (list.contains("-clearAllMaps")) {
    			HazelcastMapUtil.clearAllMap();
    		}
    		
    		if (list.contains("-destroyAllMaps")) {
    			HazelcastMapUtil.destroyAllMap();
    		}
    		
			exitCommandLine(0);
    }

}
