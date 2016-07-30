package jat.application.orbitviewer;

public class Configuration {
	public int default_numbers = 1;
	public int random_numbers = 1;
	public int user_config_numbers = 0;
	public double[] parameter = new double[6];
	orbitviewerGUI d;
	
	public Configuration() {
		default_numbers = 0;
		random_numbers = 48;
		user_config_numbers = 0;
		parameter[0] = 8000.0;
		parameter[1] = 0.1;
		parameter[2] = 15.0;
		parameter[3] = 0.0;
		parameter[4] = 0.0;
		parameter[5] = 0.0;
	//	this.d = new orbitviewerGUI();
		
	}
	
	public orbitviewerGUI getd() {
		return this.d;
	}
}