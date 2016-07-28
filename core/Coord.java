package core;

/**
 * Class to hold 3D coordinates and perform simple arithmetics and
 * transformations
 */
public class Coord
{
	/** x-coordinate*/
	private double x;
	/** y-coordinate*/
	private double y;
	/** z-coordinate*/
	private double z;
	
	/**
	 * Constructor.
	 * @param x Initial X-coordinate
	 * @param y Initial Y-coordinate
	 * @param z Initial Z-coordinate
	 */
	public Coord(double x, double y,double z) {
		setLocation(x,y,z);
	}
	
	/**
	 * Sets the location of this coordinate object
	 * @param x The x coordinate to set
	 * @param y The y coordinate to set
	 * @param z The z coordinate to set
	 */
	public void setLocation(double x, double y,double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Sets this coordinate's location to be equal to other
	 * coordinates location
	 * @param c The other coordinate
	 */
	public void setLocation(Coord c) {
		this.x = c.x;
		this.y = c.y;
		this.z = c.z;
	}
	
	/**
	 * Returns the x coordinate
	 * @return x coordinate
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the y coordinate
	 * @return y coordinate
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Returns the z coordinate
	 * @return z coordinate
	 */
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Moves the point by dx and dy and dz
	 * @param dx How much to move the point in X-direction
	 * @param dy How much to move the point in Y-direction
	 * @param dz How much to move the point in Z-direction
	 */
	public void translate(double dx, double dy, double dz) {
		this.x += dx;
		this.y += dy;
		this.z += dz;
	}
	
	/**
	 * Returns the distance to another coordinate
	 * @param other The other coordinate
	 * @return The distance between this and another coordinate
	 */
	public double distance(Coord other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		double dz = this.z - other.z;
		
		return Math.sqrt(dx*dx + dy*dy +dz*dz);
	}
	
	/**
	 * Returns the square of the distance to another coordinate
	 * @param other The other coordinate
	 * @return The square distance between this and another coordinate
	 */
	public double distance2(Coord other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		double dz = this.z - other.z;
		
		return (dx*dx + dy*dy + dz*dz);
	}
	
	/**
	 * Returns the angle (in radians) from this coord1 to the given coord1
	 * @param other The other coordinate
	 * @return The angle from this coord1 to the other coord1
	 */
	public double angle(Coord other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		
		return Math.atan2(dy, dx);
	}

	/**
	 * Returns a clone of this coordinate
	 * @return the clone of this coordinate
	 */
	public Coord clone() {
		Coord clone = null;
		try {
			clone = (Coord) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return clone;
	}
	
	/**
	 * Returns a text representation of the coordinate (rounded to 2 decimals)
	 * @return a text representation of the coordinate
	 */
	public String toString() {
		return String.format("(%.2f,%.2f,%.2f)",x,y,z);
	}
	
	/**
	 * Checks if this coordinate's location is equal to other coordinate's
	 * @param c The other coordinate
	 * @return True if locations are the same
	 */
	public boolean equals(Coord c) {
		if (c == this) {
			return true;
		}
		else {
			return (x == c.x && y == c.y && z == c.z);
		}
	}
	
	/**
	 * Returns a hash code for this coordinate
	 * (actually a hash of the String made of the coordinates)
	 */
	public int hashCode() {
		return (x+","+y).hashCode();
	}
	
	/**
	 * Compares this coordinate to other coordinate. Coordinate whose y
	 * value is smaller comes first and if y values are equal, the one with
	 * smaller x value comes first and if x values are equal, the one with
	 * smaller z values comes first.
	 * @return -1, 0 or 1 if this node is before, in the same place or
	 * after the other coordinate
	 */
	public int compareTo(Coord other) {
		if (this.y < other.y) {
			return -1;
		}
		else if (this.y > other.y) {
			return 1;
		}
		else if (this.x < other.x) {
			return -1;
		}
		else if (this.x > other.x) {
			return 1;
		}
		else if (this.z < other.z) {
			return -1;
		}
		else if (this.z > other.z) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Returns the a 3-D coordinate from a 2-D coordinate;
	 * copy the x,y of 2-D coordinate to the 3-D coordinate,
	 * then let z of 3-D coordinate is 0.
	 * @param other The other coordinate
	 * @return 3-D coordinate
	 */
	public Coord convert(Coord2 other) {
		Coord coo = new Coord(0,0,0);
		coo.setLocation(other.getX(), other.getY(),0);
		return coo;
	}
}