package com.bpsi.hazelcast.util;

import com.bpsi.hazelcast.client.HazelcastClientExtension;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collection;
import java.util.List;

import org.json.JSONException;


public class HazelcastMapUtil {
	
	private static HazelcastInstance hazelClientInstance;
	
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
		System.out.println("-setJSONConfig : Refers to JSON filename containing below cluster details");
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
	
    public static void main(String[] args) {
    		
    		List<String> list = Arrays.asList(args);
    		List<String> argList = new ArrayList<String>();
    		
    		argList.add("-destroyAllMaps");
    		argList.add("-clearAllMaps");
    		argList.add("-setJSONConfig");
    		
    		int setJSONConfigIndex = list.indexOf("-setJSONConfig");
    		System.out.println("Set config index: "+setJSONConfigIndex);
    		
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

    		if (setJSONConfigIndex != -1 ) {
    			try {
    			 hazelClientInstance = HazelcastClientExtension.createHazelClient(list.get(setJSONConfigIndex+1));
    			}
    			catch(IOException e) {
    				exitCommandLine(-3);
    			}
    			catch(JSONException e) {
    				exitCommandLine(-3);
    			}
    		}
    		else {
    			hazelClientInstance = HazelcastClientExtension.createHazelClient();;
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
