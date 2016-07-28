package jat.orbit;

/**
 * Copyright(C),2016-2020,USTC.
 * FileName: SatelliteOrbitMain.java
 * 
 * test class,test the Satellite class
 * 
 * @author XiJianXu
 * @Date 2016-06-03
 * @version 1.0
 */
public class SatelliteOrbitMain {
	/** time tf*/
	static double tf=0.0;
	/** satellite coordinate*/
	static double[][] coordinate = new double[1][3];
	/** object of SatelliteOrbit*/ 
	private static SatelliteOrbit saot;
	
	/**
	 * the main function of this calss
	 * @param args command line param
	 */
	public static void main(String[] args) {
		//call the default constructor
		saot=new SatelliteOrbit();
		//get tf moment coordinate
		coordinate = saot.getSatelliteCoordinate(tf);
	}
}