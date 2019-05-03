package app;

import java.awt.Color;
import java.util.LinkedList;

import priorityQueue.HeapListPriorityQueue;
import util.HeuristicComparator;

public class Grid 
{
	public static Color EMPTY_COLOR = new Color(240, 240, 240);
	public static int[][] DIRECTIONS = 
		{{ 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 }};// right, up, left, down
	public static int ROWS = 7, COLS = 7;
	public int nEmptyCells = ROWS*COLS;

	public GridCell[][] gridCells = new GridCell[ROWS][COLS];
	public LinkedList<GridCell> initialFlowPointers = new LinkedList<>();
	public HeapListPriorityQueue<Integer, GridCell> pq = new HeapListPriorityQueue<>(new HeuristicComparator());
	public LinkedList<LinkedList<GridCell>> finishedPaths = new LinkedList<LinkedList<GridCell>>();
	
	public GridCell[][] getGridCells() { return gridCells; }
	public LinkedList<GridCell> getInitialFlowPointers() { return initialFlowPointers; }
	public Grid() {}
	
	public boolean validPosition(Pos pos) {
		return (pos.row < Grid.ROWS && pos.col < Grid.COLS) 
				&& (pos.row >= 0 && pos.col >= 0);
	}
	
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
