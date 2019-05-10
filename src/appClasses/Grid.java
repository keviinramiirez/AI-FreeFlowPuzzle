package appClasses;

import java.awt.Color;
import java.util.LinkedList;
import java.util.PriorityQueue;

import util.HeuristicComparator;

public class Grid 
{
	public static Color EMPTY_COLOR = new Color(240, 240, 240);
	/** color used when analyzing another region to see if it's constraint */
	public static Color NON_CONSTRAINT_COLOR = new Color(99, 123, 221);
	public static int[][] DIRECTIONS = 
		{{ 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 }};// right, up, left, down
	
	public int ROWS, COLS;

	public int nEmptyCells;
	

	/* Matrix containing grid cell elements */
	public GridCell[][] gridCells;
	
	/** Contains the the four cells on the edge of the grid. */
	public LinkedList<GridCell> edges = new LinkedList<GridCell>();
	
	/* Unordered set of all initial flow pointers */
	public LinkedList<GridCell> initialFlowPointers = new LinkedList<>();
	
	/* Contains current pointers that will be moved through the grid. Key: heuristic function value, Value: Grid Cell */
	public PriorityQueue<GridCell> pq = new PriorityQueue<GridCell>(new HeuristicComparator());
	public LinkedList<LinkedList<GridCell>> finishedPaths = new LinkedList<LinkedList<GridCell>>();
	
	public Grid() { this(5, 5); }
	public Grid(int rows, int cols) {
		this.ROWS = rows;
		this.COLS = cols;
		this.nEmptyCells = ROWS*COLS;
		gridCells = new GridCell[ROWS][COLS];
	}

	
	public void storeEdges() {
		edges.add(gridCells[0][0]);
		edges.add(gridCells[0][COLS-1]);
		edges.add(gridCells[ROWS-1][0]);
		edges.add(gridCells[ROWS-1][COLS-1]);
	}

	public GridCell[][] getGridCells() { return gridCells; }
	public LinkedList<GridCell> getInitialFlowPointers() { return initialFlowPointers; }
	
	/**
	 * @param pos: position of a cell
	 * @return boolean if cell is within dimensions of grid
	 */
	public boolean validPosition(Pos pos) {
		return (pos.row < ROWS && pos.col < COLS) 
				&& (pos.row >= 0 && pos.col >= 0);
	}
	
	/**
	 * Receives row and col as parameters, converts to Pos type and checks whether it is valid or not
	 * @param row: int, row of cell
	 * @param col: int, col of cell
	 * @return boolean
	 */
	public boolean validPosition(int row, int col) {
		return validPosition(new Pos(row, col));
	}
	
	public String toString() {
		String sGrid = "[\n  [";
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				String s = " ";
				if (gridCells[r][c].isColoredCell())
					s = this.colorStr(gridCells[r][c]);
				
				sGrid += s + ((c == COLS-1) ? "]": ",");
			}
			sGrid += "\n" + ((r == ROWS-1) ? "]" : "  [");
		}
		return sGrid;
	}
	
	
	public String colorStr(GridCell cell) {
		Color color = cell.color;
		String c = " ";
		if (color.equals(Color.orange))
			c = "o";
		else if (color.equals(Color.green))
			c = "g";
		else if (color.equals(Color.red))
			c = "r";
		else if (color.equals(Color.yellow))
			c = "y";
		else if (color.equals(Color.blue))
			c = "b";
		else if (color.equals(Color.cyan))
			c = "c";
		else if (color.equals(Color.magenta))
			c = "m";
		else if (color.equals(NON_CONSTRAINT_COLOR))
			c = "*";
		
		return cell.isInitialPointer() ? c.toUpperCase() : c;
	}
	
	/** Clones each of this grid's cells */
	public GridCell[][] cloneGridCells(GridCell[][] gridCells) {
		GridCell[][] clone = new GridCell[gridCells.length][gridCells[0].length];
		
		for (int r = 0; r < gridCells.length; r++)
			for (int c = 0; c < gridCells[0].length; c++)
				clone[r][c] = gridCells[r][c].clone(this);
		
		return clone;
	}

	/** Returns this Grid with a shallow copy of this Grid Cells. */
	public Grid clone() {
		Grid clone =  new Grid(ROWS, COLS);
		clone.gridCells = new GridCell[ROWS][COLS];
		clone.edges = this.edges;
		clone.initialFlowPointers = this.initialFlowPointers;
		clone.pq = this.pq;
		clone.finishedPaths = this.finishedPaths;
		return clone;
	}
}
