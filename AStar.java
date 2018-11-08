import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

class AStar {

  	private String path;
  	private String heuristic;
	private boolean verbose;
	private char[][] grid; // the grid read in from file
	private char[][] endgrid; // grid showing explored spaces
	private Coord start; // start location
	private Coord goal; // end location
	private Set<Coord> explored; // cells visited
	private PriorityQueue<Dijkstra> agenda; // cells to visit
	private int length; // length of the path


	/**
	 * Constructor for an A* object
	 * @param args arguments for running the program
	 **/
	public AStar(String[] args) {
	  	start = new Coord();
		goal = new Coord();
		length = 0;
		explored = new HashSet<>();
		agenda = new PriorityQueue<Dijkstra>();
		processArgs(args);
	}

	//process arguments
	//     	<file>
	//     	manhattan
	//     	euclidean
	//     	options (-v -q etc)
	/**
	 * Processes arguments for running the program
	 * @param args arguments for running the program
	 **/
	public void processArgs(String[] args) {
	  	try {
			if (args[0].toLowerCase().equals("-v")) {
				verbose = true;
				path = args[1];
				if (args.length > 2) {
					heuristic = args[2];
				}
			} else {
				path = args[0];
				if (args.length > 1) {
					heuristic = args[1];
				}
			}
			heuristic = heuristic.toLowerCase();
			if (heuristic.equals("m")) {
				heuristic = "manhattan";
			}
			if (heuristic.equals("e")) {
				heuristic = "euclidean";
			}
		} catch(Exception e) {
		  	System.out.println("Bad arguments!");
			System.out.println(e.getMessage());
			System.out.println("Attempting to continue");
		}
	}

	/**
	 * Reads a grid in from a file
	 **/
	public void gridFromFile() {
		//open file
		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
	
			//read dimensions from first line
			line = br.readLine();
			String[] nums = line.split(" ");
			grid = new char[Integer.parseInt(nums[0])][Integer.parseInt(nums[1])];
			endgrid = new char[Integer.parseInt(nums[0])][Integer.parseInt(nums[1])];
	
			//read grid
		  	for (int y = 0; y < grid[0].length; y++) {
				line = br.readLine();
				for (int x = 0; x < grid.length; x++) {
					grid[x][y] = line.charAt(x);
					endgrid[x][y] = line.charAt(x);
				}
			}
			fr.close();
		} catch (Exception e) {
			System.out.println("read died :(");
			System.out.println(e.getMessage());
			// backup grid
			grid = new char[1][1];
			grid[0][0] = ' ';
		}
	}
	
	//TODO process grid
	public void processGrid() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				if (grid[x][y] == 'o') {
//				  	System.out.println("| found start at "+x+","+y); //DEBUG
					start.set(x,y);
				} else if (grid[x][y] == '*') {
					goal.set(x,y);
//				  	System.out.println("| found goal at "+x+","+y); //DEBUG
				}
			}
		}
		// add the start node so we can begin search
		Dijkstra d = new Dijkstra(start);
		d.findG(start, heuristic);
		d.findH(goal, heuristic);
		//System.out.println(d);//DEBUG
		agenda.add(d);
	}
	
	/**
	 * Finds a path from the start to the goal
	 * Currently only checks tiles directly adjacent
	 * @return length length of the path
	 **/
	public int findPath() {

	  	if (explored.size() < 1) {
			explore(agenda.poll());
		}

	  	while (!agenda.isEmpty()) {
			Dijkstra node = agenda.poll(); // get the current most promising node
//			System.out.println("Checking "+node);//DEBUG
			if (node.equals(goal)) {
//			  	System.out.println("SUCCESS: node is goal!!!");//DEBUG
				System.out.println("Showing path");
				while (node != null) {
				  	System.out.println(node);
					if (length != 0 && node.previous != null) {
						endgrid[node.x][node.y] = '+';
					}
					node = node.previous;
					length++;
				}
				length--;
				break;
			}
			
			if (!explored.contains(new Coord(node.x, node.y)) && grid[node.x][node.y] != '#') {
//				System.out.println("Exploring");//DEBUG
				explore(node);
			}
		}
//		System.out.println("Agenda is empty");//DEBUG

		return length;
	}

	//TODO explore, documentation
	private void explore(Dijkstra node) {	
	  	
//	  	System.out.println("Exploring "+node);

		explored.add(node);

	  	ArrayList<Dijkstra> temp = new ArrayList<Dijkstra>();
	  
	  	if (node.x > 0) {
//		  	System.out.println("\tchecking left");//DEBUG
			Dijkstra  d = new Dijkstra(node.x-1, node.y);
			d.findG(start, heuristic);
			d.findH(goal, heuristic);
			d.previous = node;
			temp.add(d);
		}
	  	if (node.x < grid.length-1) {
//		  	System.out.println("\tchecking right");//DEBUG
			Dijkstra  d = new Dijkstra(node.x+1, node.y);
			d.findG(start, heuristic);
			d.findH(goal, heuristic);
			d.previous = node;
			temp.add(d);
		}
	  	if (node.y > 0) {
//		  	System.out.println("\tchecking up");//DEBUG
		  	Dijkstra  d = new Dijkstra(node.x, node.y-1);
			d.findG(start, heuristic);
			d.findH(goal, heuristic);
			d.previous = node;
			temp.add(d);
		}
	  	if (node.y < grid[node.x].length-1) {
//		  	System.out.println("\tchecking down");//DEBUG
			Dijkstra  d = new Dijkstra(node.x, node.y+1);
			d.findG(start, heuristic);
			d.findH(goal, heuristic);
			d.previous = node;
			temp.add(d);
		}

		// euclidean corner handling
		if (heuristic.equals("euclidean")) {
		  	if (node.x > 0 && node.y > 0) {
				Dijkstra  d = new Dijkstra(node.x-1, node.y-1);
				d.findG(start, heuristic);
				d.findH(goal, heuristic);
				d.previous = node;
				temp.add(d);
			}
			if (node.x > 0 && node.y < grid[node.x].length-1) {
				Dijkstra  d = new Dijkstra(node.x-1, node.y+1);
				d.findG(start, heuristic);
				d.findH(goal, heuristic);
				d.previous = node;
				temp.add(d);
			}
			if (node.x < grid.length-1 && node.y > 0) {
				Dijkstra  d = new Dijkstra(node.x+1, node.y-1);
				d.findG(start, heuristic);
				d.findH(goal, heuristic);
				d.previous = node;
				temp.add(d);
			}
			if (node.x < grid.length-1 && node.y < grid[node.x].length-1) {
				Dijkstra  d = new Dijkstra(node.x+1, node.y+1);
				d.findG(start, heuristic);
				d.findH(goal, heuristic);
				d.previous = node;
				temp.add(d);
			}
		}

		if (grid[node.x][node.y] == '.') {
			endgrid[node.x][node.y] = '-';
		}

		for (Dijkstra t : temp) {
			boolean isExplored = false;
			for (Coord e : explored) {
				if (e.x == t.x && e.y == t.y) {
					isExplored = true;
//					System.out.println("Node "+t+" already explored");//DEBUG
				}
			}
			if (!isExplored) {
//			  	System.out.println("Adding node "+t+" to agenda");//DEBUG
				//agenda.add(t);
				boolean isAgendaed = false;
				for (Dijkstra a : agenda) {
					if (a.equals(t)) {
						isAgendaed = true;
					}
				}
				if (!isAgendaed) {
					agenda.add(t);
				}
			}
		}

//		System.out.print("Explored "+node.x+","+node.y);//DEBUG
//		if (node.previous != null) {
//			System.out.print(" from "+node.previous.x+","+node.previous.y);//DEBUG
//		}
		if (verbose) {
			System.out.println("Step:\n"+toString()+"\n");//DEBUG VERBOSE
			/*
			try { // DEBUG
				Thread.sleep(200);
			} catch (Exception e) {

			}
			*/
		}
	}


	//TODO documentation
	public void run() {
		gridFromFile();
		processGrid();
		length = findPath();
	}

	public String toString() {
		String result = "";
		for (int y = 0; y < endgrid[0].length; y++) {
			for (int x = 0; x < endgrid.length; x++) {
				result += endgrid[x][y];//+" ";
			}
			result += "\n";
		}

		if (length < 1) {
			result += "No path";
		} else {
			result += "Path found with length "+length;
		}

		return result;
	}

	public static void main(String[] args) {
		AStar a = new AStar(args);
		a.run();
		System.out.println("\n\nFinal\n"+a);
	}

}
