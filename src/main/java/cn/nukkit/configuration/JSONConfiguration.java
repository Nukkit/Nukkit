package main.java.cn.Nugget.configuration;

import java.io.File;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONConfiguration{
	//Static Functions
	public static JSONConfiguration createBlankJSON(){
		JSONArray jsonArray = JSONArray.fromObject("[]");
		return new JSONConfiguration(jsonArray);
	}
	public static JSONConfiguration loadConfiguration(File config, FileEncoding encoding) {
		String context = FileLoader.readFile(config, encoding);
		JSONArray jsonArray = JSONArray.fromObject(context);
		return new JSONConfiguration(jsonArray);
	}
	
	//Others
	private int size;
	private JSONArray array;
	private JSONConfiguration(JSONArray arrayz){
		array = arrayz;
		size = array.size();
	}
	
	/*
	 * returning Objects
	 */
	public Object get(String path){
		return get(path, 0);
	}
	public Object get(String path, int index){
		if(index < size){
			JSONObject obj = array.getJSONObject(index);
			return obj.get(path);
		}
		return null;
	}
	
	//String
	public String getString(String path){
		return getString(path, 0);
	}
	public String getString(String path, int index){
		return (String)get(path, index);
	}
	
	//Boolean
	public boolean getBool(String path){
		return getBool(path, 0);
	}
	public boolean getBool(String path, int index){
		return (boolean)get(path, index);
	}
	
	//Float
	public float getFloat(String path){
		return getFloat(path, 0);
	}
	public float getFloat(String path, int index){
		return (float)get(path, index);
	}
	
	//Double
	public double getDouble(String path){
		return getDouble(path, 0);
	}
	public double getDouble(String path, int index){
		return (Double)get(path, index);
	}
}
