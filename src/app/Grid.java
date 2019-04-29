package app;

import java.awt.Color;
import java.util.HashSet;

public class Grid 
{
	public static Color EMPTY_COLOR = new Color(240, 240, 240);
	public int[][] DIRECTIONS = 
		{{ 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 }};// right, up, left, down
	
	public static int ROWS = 10, COLS = 10;
	GridCell[][] gridCells = new GridCell[ROWS][COLS];
	HashSet<Pos> initialFlowPointerPos = new HashSet<>();

	public Grid() {}
}
