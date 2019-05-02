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
	
	public boolean validPosition(Pos pos) {
		return (pos.row < Grid.ROWS && pos.col < Grid.COLS) 
				&& (pos.row >= 0 && pos.col >= 0);
	}
	
	public boolean validPosition(int row, int col) {
		return validPosition(new Pos(row, col));
	}
}
