/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package movement;

import core.Coord2;
import core.Settings;

/**
 * Random waypoint movement model. Creates zig-zag paths within the
 * simulation area.
 */
public class RandomWaypoint extends MovementModel {
	/** how many waypoints should there be per path */
	private static final int PATH_LENGTH = 1;
	private Coord2 lastWaypoint;
	
	public RandomWaypoint(Settings settings) {
		super(settings);
	}
	
	protected RandomWaypoint(RandomWaypoint rwp) {
		super(rwp);
	}
	
	/**
	 * Returns a possible (random) placement for a host
	 * @return Random position on the map
	 */
	@Override
	public Coord2 getInitialLocation() {
		assert rng != null : "MovementModel not initialized!";
		Coord2 c = randomCoord();

		this.lastWaypoint = c;
		return c;
	}
	
	@Override
	public Path getPath() {
		Path p;
		p = new Path(generateSpeed());
		p.addWaypoint(lastWaypoint.clone());
		Coord2 c = lastWaypoint;
		
		for (int i=0; i<PATH_LENGTH; i++) {
			c = randomCoord();
			p.addWaypoint(c);	
		}
		
		this.lastWaypoint = c;
		return p;
	}
	
	@Override
	public RandomWaypoint replicateDefault() {
		return new RandomWaypoint(this);
	}
	
	public SatelliteMovement replicateRandom(boolean multi_orbit) {
		return new SatelliteMovement();
	}
	
	public SatelliteMovement replicateRandom(boolean multi_orbit,int order) {
		return new SatelliteMovement();
	}
	
	public SatelliteMovement replicateUserConfig(double[] t) {
		return new SatelliteMovement();
	}
	
	protected Coord2 randomCoord() {
		return new Coord2(rng.nextDouble() * getMaxX(),
				rng.nextDouble() * getMaxY());
	}
}
