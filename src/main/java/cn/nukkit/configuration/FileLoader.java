package main.java.cn.Nugget.configuration;

import java.io.*;
public class FileLoader {
	public static String readFile(File cfg, FileEncoding encode){
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fis = new FileInputStream(cfg);
			InputStreamReader isr = new InputStreamReader(fis, encode.getContext());
			reader = new BufferedReader(isr);
			String temp = null;
			while((temp = reader.readLine()) != null){
				laststr += temp;
			}
			reader.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return laststr;
	}
}
