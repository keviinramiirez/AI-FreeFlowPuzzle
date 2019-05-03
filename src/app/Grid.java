package app;

import java.awt.Color;
import java.util.LinkedList;
import priorityQueue.HeapPriorityQueue;
import util.HeuristicComparator;

public class Grid 
{
	public static Color EMPTY_COLOR = new Color(240, 240, 240);
	public static int[][] DIRECTIONS = 
		{{ 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 }};// right, up, left, down
	public static int ROWS = 10, COLS = 10;
	public int nEmptyCells = ROWS*COLS;

	public GridCell[][] gridCells = new GridCell[ROWS][COLS];
	public LinkedList<GridCell> initialFlowPointers = new LinkedList<>();
	public HeapPriorityQueue<Integer, GridCell> pq = new HeapPriorityQueue<>(new HeuristicComparator());
	public LinkedList<LinkedList<GridCell>> finishedPaths = new LinkedList<LinkedList<GridCell>>();
	
	public GridCell[][] getGridCells() { return gridCells; }
	public LinkedList<GridCell> getInitialFlowPointers() { return initialFlowPointers; }
	public Grid() {}
	
	/**
	 * @param pos: position of a cell
	 * @return boolean if cell is within dimensions of grid
	 */
	public boolean validPosition(Pos pos) {
		return (pos.row < Grid.ROWS && pos.col < Grid.COLS) 
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
		for (int r = 0; r < Grid.ROWS; r++) {
			for (int c = 0; c < Grid.COLS; c++) {
				String s = " ";
				if (gridCells[r][c].isColoredCell()) {
					s = this.colorStr(gridCells[r][c].color, 
							gridCells[r][c].isInitialFlowPointer());
				}
				
				sGrid += s + ((c == Grid.COLS-1) ? "]": ",");
			}
			sGrid += "\n" + ((r == Grid.ROWS-1) ? "]" : "  [");
		}
		return sGrid;
	}
	
	
	public String colorStr(Color color, boolean isInitialPointer) {
		String c = "";
		if (color.equals(Color.cyan))
			c = "c";
		else if (color.equals(Color.orange))
			return "o";
		else if (color.equals(Color.green))
			return "g";
		else if (color.equals(Color.red))
			return "r";
		else if (color.equals(Color.yellow))
			return "y";
		else if (color.equals(Color.blue))
			return "b";
		else if (color.equals(Color.magenta))
			return "m";
		
		return isInitialPointer ? c.toUpperCase() : c;
	}
}
