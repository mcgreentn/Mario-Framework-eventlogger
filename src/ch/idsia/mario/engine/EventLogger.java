package ch.idsia.mario.engine;

import java.util.Set;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EventLogger {
	
	static Set<String> previous_actions = new HashSet<String>();
	static JSONObject obj = new JSONObject();
	static JSONParser parser = new JSONParser();
	static JSONArray actions = new JSONArray();
	
	static int currTimeStamp = 0;
	
	static String fn = "event_log_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	
	static int world_num = 1;
	
	static String filename = fn + "_" + world_num + ".json";
	
	@SuppressWarnings("unchecked")
	public static void addToLogger(String action, int status, float x, float y, int timeStamp) {
//		System.out.println(action + " " + timeStamp);
		
		//if (!previous_actions.contains(action)) {
			//check if it is a new timeStamp
			if(timeStamp != currTimeStamp) {
				writeLogger(); 
				currTimeStamp = timeStamp;
				actions = new JSONArray();
			} 
			ArrayList<String> to_log = new ArrayList<String>(); 
			to_log.add("Action: " + action);
			to_log.add("Status: " + status);
			to_log.add("X: " + x);
			to_log.add("Y " + y);
			to_log.add("Time: " + timeStamp);
			actions.add(to_log);
			
			previous_actions.add(action);
		//}
	}
	
	@SuppressWarnings("unchecked")
	public static void readJson(String filename) {
		JSONObject written_ob = null;
		try {
			written_ob = (JSONObject) parser.parse(new FileReader(filename));
		} catch (ParseException | IOException e) {
			File file = new File(filename);
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (written_ob != null) {
			written_ob.put(currTimeStamp, actions);
			obj = written_ob;
		} else {
			obj.put(currTimeStamp, actions);
		}
	}
	
	public static void writeLogger() {
		if (actions.size() == 0) {
			if (currTimeStamp == 0) {
				return;
			}
		}
		readJson(filename);
		try (FileWriter file = new FileWriter(filename, false)) {
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("JSON Object: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writeLogger(Boolean ending) {
		readJson(filename);
		try (FileWriter file = new FileWriter(filename, false)) {
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("JSON Object: " + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//that world/run is done
		//reset vars
		world_num += 1;
		filename = fn + "_" + world_num + ".json";
		obj = new JSONObject();
		actions = new JSONArray();
		currTimeStamp = 0;
		previous_actions = new HashSet<String>();
	}
	
}
