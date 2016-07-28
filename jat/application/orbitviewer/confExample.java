package jat.application.orbitviewer;

import core.Settings;
import core.SettingsError;
import core.SimScenario;

import java.io.IOException;

public class confExample {
	
	private static final String CONFIGURATION_FILE_LOCATION = 
			"C:\\Users\\Administrator\\Desktop\\one\\src\\one_\\default_settings.txt";
	
	Settings settings = null;
	String[] conf = new String[2];
	
	public static void main(String[] args) throws IOException{
		new confExample();
	}
	
	public confExample() throws IOException{
		settings = new Settings();
	//	settings.init(null);
		conf[0] = "name";
		conf[1] = "user";
		settings.setSetting(CONFIGURATION_FILE_LOCATION,"---------------------name----------------------------------------------------","---user---");
		settings.setSetting(CONFIGURATION_FILE_LOCATION,"---------------------name-------------------------------","---user---");
	//	String str = settings.getSetting("---------------------name----------------------------------------------------");
	//	System.out.println("test "+ str);
	}
	
	
}

