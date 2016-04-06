package org.squirrel.utill;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class OptionSaver {
	private static Properties prop = new Properties();
	private static InputStream input = null;
	private static OutputStream output = null;
	public static String outputString = "";
	
	public static void writeProp(String file, String key, String value) throws IOException{
		try {
			output = new FileOutputStream(file);
			
			prop.setProperty(key, value);
			prop.store(output, null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static String readProp(String file, String key) throws IOException{
		try {
			input = new FileInputStream(file);
			prop.load(input);
			
			outputString = prop.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return outputString;
	}
}
