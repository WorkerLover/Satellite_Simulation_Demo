package movement;

import java.util.Random;

import util.ActivenessHandler;

import core.Coord2;
import core.Coord;
import core.DTNHost;
import core.DTNSim;
import core.ModuleCommunicationBus;
import core.Settings;
import core.SimClock;
import core.SimError;

import jat.orbit.SatelliteOrbit;

/**
 * Copyright(C),2016-2020,USTC.
 * FileName: SatelliteMovement.java
 * 
 * Satellite Movement Model.
 * 
 * @author XiJianXu
 * @Date 2016-06-03
 * @version 1.0
 */
public class SatelliteMovement extends MovementModel {
	private Coord2 cod;
	/** satellite 3-D coordinate*/
	private Coord coordinate;
	/** object of SatelliteOrbit class*/
	private SatelliteOrbit saot;
	
	/**
	 * default constructor
	 */
	public SatelliteMovement()
	{
	//	super(settings);
		cod = new Coord2(0,0);
		coordinate = new Coord(0,0,0);
		//default constructor
		saot = new SatelliteOrbit();
	}
	
	/**
	 * User Config constructor
	 * @param t param of user config
	 */
	public SatelliteMovement(double[] t) {
		cod = new Coord2(0,0);
		coordinate = new Coord(0,0,0);
		//user_config constructor
		saot = new SatelliteOrbit(t);
	}
	
	/**
	 * Random param constructor
	 * @param multi_orbit if multi_orbit is true,
	 * create orbit of random param;if multi_orbir
	 * is false,create orbit of default param 
	 */
	public SatelliteMovement(boolean multi_orbit) {
		cod = new Coord2(0,0);
		coordinate = new Coord(0,0,0);
		//random constructor
		saot = new SatelliteOrbit(multi_orbit);
	}
	
	/**
	 * Random param constructor
	 * @param multi_orbit if multi_orbit is true,
	 * create orbit of random param;if multi_orbir
	 * is false,create orbit of default param
	 * @param order add order message
	 */
	public SatelliteMovement(boolean multi_orbit,int order) {
		cod = new Coord2(0,0);
		coordinate = new Coord(0,0,0);
		//random constructor
		saot = new SatelliteOrbit(multi_orbit,order);
	}
	
	/**
	 * through settings constructor
	 * @param settings settings
	 */
	public SatelliteMovement(Settings settings)
	{
		super(settings);
		cod = new Coord2(0,0);
		coordinate = new Coord(0,0,0);
		//default constructor
		saot = new SatelliteOrbit();
	}
	
	/**
	 * copy constructor
	 * @param smt other SatelliteMovement
	 */
	private SatelliteMovement(SatelliteMovement smt)
	{
		super(smt);
		cod = smt.cod;
		coordinate = smt.coordinate;
		saot = smt.saot;
	}
	
	/**
	 * get initial coordinate
	 * @return coordinate
	 */
	public Coord2 getInitialLocation()
	{
		return this.cod;
	}
	
	/**
	 * get satellite 3-D initial coordinate
	 * @return satellite initial location
	 */
	public Coord getInitialLocation1() {
		double[][] t = new double[1][3];
		t = saot.getInitLocation();
		this.coordinate.setLocation(t[0][0],t[0][1],t[0][2]);
		
		return this.coordinate;
	}
	
	/**
	 * get satellite immediate 3-D coordinate
	 * @param t time 
	 * @return satellite immediate location
	 */
	public Coord getSatelliteLocation(double t) {
		double[][] coor= new double[1][3];
		coor = saot.getSatelliteCoordinate(t);
		this.coordinate.setLocation(coor[0][0],coor[0][1],coor[0][2]);
		
		return this.coordinate;
	}
	
	/**
	 * get path
	 * @return list of location
	 */
	public Path getPath() {
		//default constructor
		Path p = new Path();
		return p;	
	}
	
	/**
	 * overwrite replicateDefault function
	 * @return SatelliteMovement object
	 */
	public SatelliteMovement replicateDefault() {
		//default constructor
		return new SatelliteMovement();
	}
	
	/**
	 * overwrite replicateRandom function
	 * @return SatelliteMovement object
	 */
	public SatelliteMovement replicateRandom(boolean multi_orbit) {
		//random constructor
		return new SatelliteMovement(multi_orbit);
	}
	
	/**
	 * overwrite replicateRandom function.And add the order message
	 * @return SatelliteMovement object
	 */
	public SatelliteMovement replicateRandom(boolean multi_orbit,int order) {
		//random constructor
		return new SatelliteMovement(multi_orbit,order);
	}
	
	/**
	 * overwrite replicateUserConfig function
	 * @return SatelliteMovement object
	 */
	public SatelliteMovement replicateUserConfig(double[] t) {
		//user_config constructor
		return new SatelliteMovement(t);
	}
	
	/**
	 * get name of movement
	 * @return name
	 */
	public String getMovementName() {
		return "SatelliteMovement";
	}
	
	/**
	 * get orbit
	 * @return SatelliteOrbit object
	 */
	public SatelliteOrbit getOrbit() {
		return saot;
	}
}