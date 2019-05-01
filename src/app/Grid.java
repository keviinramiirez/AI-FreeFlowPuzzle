package app;

import java.awt.Color;
import java.util.HashSet;

import priorityQueue.HeapPriorityQueue;
import util.HeuristicComparator;

public class Grid 
{
	public static Color EMPTY_COLOR = new Color(240, 240, 240);
	public static int[][] DIRECTIONS = 
		{{ 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 }};// right, up, left, down
	public static int ROWS = 10, COLS = 10;
	public int nEmptyCells = ROWS*COLS;
	
	public GridCell[][] gridCells = new GridCell[ROWS][COLS];
	public HashSet<Pos> initialFlowPointers = new HashSet<>();
	public HeapPriorityQueue<Integer, GridCell> pq = new HeapPriorityQueue<>(new HeuristicComparator());

	public GridCell[][] getGridCells() { return gridCells; }
	public HashSet<Pos> getInitialFlowPointers() { return initialFlowPointers; }
	public Grid() {}
}
