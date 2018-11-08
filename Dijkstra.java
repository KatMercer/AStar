public class Dijkstra extends Coord implements Comparable<Dijkstra>{
	
	private double g; // this node distance from start
	private double h; // this node distance from end
	private double f; // estimated distance via this node
	public Dijkstra previous; // where we came from

	public Dijkstra() {
		super();
		f = 0;
		g = 0;
		h = 0;
	}

	public Dijkstra(int x, int y) {
		super(x, y);
		f = 0;
		g = 0;
		h = 0;
	}

	public Dijkstra(Coord c) {
		super(c.x, c.y);
		f = 0;
		g = 0;
		h = 0;
	}

	public double getF() {
	  	return this.f;
	}

	public double getG() {
		return this.g;
	}

	public double getH() {
		return this.h;
	}

	/**
	 * Sets the distance of this node from the start
	 * @param g distance from start
	 **/
	public void setG(int g) {
		this.g = g;
		calculateF();
	}

	/**
	 * Sets the distance of this node from the end
	 * @param h distance from end
	 **/
	public void setH(int h) {
		this.h = h;
		calculateF();
	}

	/**
	 * Calculates the f value based on g and h values
	 **/
	private void calculateF() {
		f = g + h;
	}

	/**
	 * Finds the g value using the specified heuristic based on the specified Coord
	 * @param start the location of the start
	 * @param heuristic the heuristic to use
	 **/
	public void findG(Coord start, String heuristic) {
		if (heuristic.equals("euclidean")) { // Euclidean
			g = Math.sqrt(Math.pow(this.x-start.x, 2)+Math.pow(this.y-start.y, 2));
		} else { // Manhattan (default)
			g = Math.abs(this.x - start.x) + Math.abs(this.y - start.y);
		}
		calculateF();
	}

	/**
	 * Finds the h value using the specified heuristic based on the specified Coord
	 * @param end the location of the end
	 * @param heuristic the heuristic to use
	 **/
	public void findH(Coord end, String heuristic) {
		if (heuristic.equals("euclidean")) { // Euclidean
			h = Math.sqrt(Math.pow(this.x-end.x, 2)+Math.pow(this.y-end.y, 2));
		} else { // Manhattan (default)
			h = Math.abs(this.x - end.x) + Math.abs(this.y - end.y);
		}
		calculateF();
	}

	/**
	 * Compares this object with the specified object for order
	 * @param o the object to compare to
	 * @return negative integer, zero, or positive integer as this object is less
	 * 	than, equal to, or greater than the specified object
	 **/
	@Override
	public int compareTo(Dijkstra o) {
		return (int)this.getF() - (int)o.getF();
	}

	public String toString() {
		String result = "";
		result += "("+x+","+y+")";
		//result += " "+f+"="+g+"+"+h;
		if (previous != null) {
			result += " from "+"("+previous.x+","+previous.y+")";
		}
		return result;
	}

}
