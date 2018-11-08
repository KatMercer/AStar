public class Coord{
	public int x;
	public int y;

	/* Other functions go here */

	/**
	 * Constructor for the Coord class
	 **/
	public Coord() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Constructor for the Coord class
	 * @param x the x coordinate to add
	 * @param y the y coordinate to add
	 **/
	public Coord(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds a coordinate to this coordinate
	 * @param other the other coordinate to add
	 **/
	public void add(Coord other) {
		this.x += other.x;
		this.y += other.y;
	}

	/**
	 * Adds an x and y value to this coordinate
	 * @param x the x coordinate to add
	 * @param y the y coordinate to add
	 **/
	public Coord add(int x, int y) {
		this.x += x; 
		this.y += y;
		return this;
	}

	/**
	 * Subtracts a coordinate from this coordinate
	 * @param other the other coordinate to subtract
	 **/
	public void sub(Coord other) {
		this.x -= other.x;
		this.y -= other.y;
	}

	/**
	 * Subtracts a coordinate from another coordinate and returns the result
	 * @param target the coord to subtract from
	 * @param other the coord to subtract
	 **/
	public static Coord sub(Coord target, Coord other) {
		return new Coord(target.x-other.x, target.y-other.y);
	}


	/**
	 * Sets the coordinate to the specified x and y
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 **/
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Evaluates the equality of this Coord with another
	 * @param other the Coord to compare to
	 **/
	public boolean equals(Coord other) {
		if ((this.x == other.x) && (this.y == other.y)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Caps the object that called this function to the bounds given in c
	 * @param c the maximum size of these coordinates
	 * @return this
	 **/
	public Coord inLimits(Coord c) {
		if (this.x < 0) {
		  this.x = 0;
		} else if (this.x >= c.x) {
		  this.x = c.y - 1;
		}
		if (this.y < 0) {
		  this.y = 0;
		} else if (this.y >= c.y) {
		  this.y = c.y - 1;
		}

	  	return this;
	}

	public String toString() {
		return "("+x+","+y+")";
	}

}
