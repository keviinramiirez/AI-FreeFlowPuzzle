package util;

import app.Grid;
import app.GridCell;

public class Util 
{
	public static GridCell[][] cloneGridCells(Grid grid, GridCell[][] gridCells) {
		GridCell[][] clone = new GridCell[gridCells.length][gridCells[0].length];
		
		for (int r = 0; r < gridCells.length; r++) {
			for (int c = 0; c < gridCells[0].length; c++) {
				clone[r][c] = gridCells[r][c].clone(grid);
			}
		}
		
		return clone;
	}
}
