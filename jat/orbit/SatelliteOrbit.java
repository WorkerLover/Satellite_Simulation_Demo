package jat.orbit;

import java.util.Random;

import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.cm.cm;

/** 
 * Copyright(C),2016-2020,USTC.
 * FileName:SatelliteOrbit.java
 * this class can create three kinds of satellite orbit:
 * default param orbit,usercongfig param orbit and random
 * param orbit.And through calling this class's member
 * function can get the initial coordinate of satellite
 * and immediate coordinate.
 * 
 * @author XiJianXu
 * @Date 2016-06-03
 * @version 1.0
*/

public class SatelliteOrbit implements Printable {
	/** sma in km*/
	public double a = 8000.0;
	/** eccentricity*/
	double e = 0.1;
	/** inclination in degrees*/
	double i = 15;
	/** right ascension of ascending node in degrees*/
	double raan = 0.0;
	/** argument of perigee in degrees*/
	double w = 0.0;
	/** true anomaly in degrees*/
	double ta = 0.0;
	/** step length*/
	int steps = 200;
	/** satellite coordinate*/
    double[][] satellitecoordinate = new double[1][3];
    /** satellite initial coordinate*/
    double[][] initcoordinate = new double[1][3];
    /** TwoBody model that create orbit*/ 
    private static TwoBody tb;
    
    public int satellite_numbers = 0;
    
    public int orbit_numbers = 0;
    
    public int phrase = 0;
	
    /**
     * default constructor of create satellite orbit
     * 
     */
	public SatelliteOrbit() {
		this.tb = new TwoBody(a,e,i,raan,w,ta);
		//default Constructor
	}
	
	/**
	 * user_config constructor of creating satellite 
	 * orbit,user can config orbit param 
	 * @param t the orbit param of user config
	 */
	public SatelliteOrbit(double[] t) {
		if(t.length<6||t.length>6) {
			new SatelliteOrbit();
		}
		else if(t.length==6){
			this.a = t[0];
			this.e = t[1];
			this.i = t[2];
			this.raan = t[3];
			this.w = t[4];
			this.ta = t[5];
			this.steps=200;
			this.satellitecoordinate = new double[1][3];
			this.tb = new TwoBody(a,e,i,raan,w,ta);
		}
	}
	
	/**
	 * random constructor of creating satellite orbit,
	 * this constructor can create the orbit of 
	 * random orbit param
	 * @param multiorbit if multi_orbit is true,create
	 * random orbit param;if multi_orbit is false,
	 * create default orbit param
	 */
	public SatelliteOrbit(boolean multi_orbit) {
		if(multi_orbit==true) {
			satellite_numbers = 24;
			orbit_numbers = 3;
			phrase = 2;
			int s = satellite_numbers/orbit_numbers;
			Random ne = new Random();
			this.a = 12000;//ne.nextDouble()*5000.0+8000.0;
			this.e = 0;//ne.nextDouble();
			this.i = 15;/*(360/orbit_numbers)*(ne.nextInt(orbit_numbers)-1);*///ne.nextInt(360);
			this.raan = (360/orbit_numbers)*(ne.nextInt(orbit_numbers)-1);/*(360/s)*(ne.nextInt(s-1)-1)+
					(360/satellite_numbers)*phrase*(ne.nextInt(orbit_numbers)-1);*///0.0;
			this.w = (360/s)*(ne.nextInt(s-1)-1)+
					(360/satellite_numbers)*phrase*(ne.nextInt(orbit_numbers)-1);//0.0;
			this.ta = 0.0;
			this.satellitecoordinate = new double[1][3];
			this.tb = new TwoBody(a,e,i,raan,w,ta);
		//	System.out.println("a is "+getA());
		}
		else {
			new SatelliteOrbit();
		}
	}
	//overwrite;
	public SatelliteOrbit(boolean multi_orbit,int order) {
		if(multi_orbit==true) {
			satellite_numbers = 24;
			orbit_numbers = 3;
			phrase = 2;
			int s = satellite_numbers/orbit_numbers;
		//	Random ne = new Random();
			this.a = 9000;//ne.nextDouble()*5000.0+8000.0;
			this.e = 0;//ne.nextDouble();
			this.i = 55;/*(360/orbit_numbers)*(ne.nextInt(orbit_numbers)-1);*///ne.nextInt(360);
			this.raan = (360/orbit_numbers)*(order/s);/*(360/s)*(ne.nextInt(s-1)-1)+
					(360/satellite_numbers)*phrase*(ne.nextInt(orbit_numbers)-1);*///0.0;
			this.w = (360/s)*(order-(order/s)*s-1)+
					(360/satellite_numbers)*phrase*(order/s);//0.0;
			this.ta = 0.0;
			this.satellitecoordinate = new double[1][3];
			this.tb = new TwoBody(a,e,i,raan,w,ta);
		//	System.out.println("a is "+getA());
		}
		else {
			new SatelliteOrbit();
		}
	}
	
	/**
	 * get the initial coordinate of satellite
	 * @return the initial location of satellite
	 */
	public double[][] getInitLocation() {
		double period = tb.period();
		double tf = period;
		tb.setSteps(steps);
	/*	Random n = new Random();
		double time = n.nextDouble()*tf;
		tb.propagate(time);*/
		tb.propagate(tf);
		
		initcoordinate = new double[1][3] ;
		initcoordinate[0][0] = tb.rv.x[0];
		initcoordinate[0][1] = tb.rv.x[1];
		initcoordinate[0][2] = tb.rv.x[2];
		return this.initcoordinate;
	}
	
	/**
	 * get the immediate coordinate of satellite
	 * @param t time t
	 * @return the immediate location of satellite
	 */
	public double[][] getSatelliteCoordinate(double t) {
		// find out the period of the orbit
		double period = tb.period();
		// set the final time = one orbit period
		double tf = period;
		tb.setSteps(steps);
		// propagate the orbit
		if(t>tf) {
			tb.propagate(t%tf);
		}
		else if(t<=tf) {
			tb.propagate(t);
		}
		
		satellitecoordinate = new double[1][3] ;
		satellitecoordinate[0][0] = tb.rv.x[0];
		satellitecoordinate[0][1] = tb.rv.x[1];
		satellitecoordinate[0][2] = tb.rv.x[2];
    //  System.out.println("coordinate "+ satellitecoordinate[0][0]);
        return this.satellitecoordinate;
	}
	
	public TwoBody getTwoBody() {
		return this.tb;
	}
	
	public double getA() {
		return a;
	}
	
	public double getE() {
		return e;
	}
	
	public double getI() {
		return i;
	}
	public double getRaan() {
		return raan;
	}
	
	public double getW() {
		return w;
	}
	
	public double getTa() {
		return ta;
	}

/*	public void make_plot() {
		// create your PlotPanel (you can use it as a JPanel) with a legend at
		// SOUTH
		plot = new Plot3DPanel("SOUTH");
		// add grid plot to the PlotPanel
		add_scene();
	}*/
	
	/**
	 * test function
	 */
	public void print(double t, double[] y) {

		// add data point to the plot
		// also print to the screen for warm fuzzy feeling
		System.out.println("coord ");
	}

	public void print1(double t, double[] y) {

		// add data point to the plot
		// also print to the screen for warm fuzzy feeling
		System.out.println("coord ");
	}
	
	public void print2(double t, double[] y) {

		// add data point to the plot
		// also print to the screen for warm fuzzy feeling
		System.out.println("coord ");
	}
	/*
	 * public static void main(String[] args) {
	 * 
	 * 
	 * // put the PlotPanel in a JFrame like a JPanel JFrame frame = new
	 * JFrame("a plot panel"); frame.setSize(600, 600);
	 * frame.setContentPane(plot); frame.setVisible(true);
	 * 
	 * }
	 */
}
