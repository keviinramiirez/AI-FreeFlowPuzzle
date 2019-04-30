package util;

import java.awt.Color;
import java.util.ArrayList;

import app.Grid;
import app.GridCell;
import app.Pos;

public class HardCodeFlowPointers 
{
	private Grid grid;
		
	public HardCodeFlowPointers(Grid grid) {
		this.grid = grid;
	}
	
	public ArrayList<GridCell> generateInitFlowPointers1() {
		// RED
		GridCell red28 = new GridCell(grid, new Pos(2, 8), 0, Color.red);
		GridCell red53 = new GridCell(grid, new Pos(5, 3), 0, Color.red);

		// BLUE
		GridCell blue78 = new GridCell(grid, new Pos(7, 8), 0, Color.blue);
		GridCell blue81 = new GridCell(grid, new Pos(8, 1), 0, Color.blue);

		
		// CYAN
		GridCell cyan33 = new GridCell(grid, new Pos(3, 3), 0, Color.cyan);
		GridCell cyan77 = new GridCell(grid, new Pos(7, 7), 0, Color.cyan);

		
		// ORANGE
		GridCell orange34 = new GridCell(grid, new Pos(3, 4), 0, Color.orange);
		GridCell orange68 = new GridCell(grid, new Pos(6, 8), 0, Color.orange);

		
		// GREEN
		GridCell green36 = new GridCell(grid, new Pos(3, 6), 0, Color.green);
		GridCell green72 = new GridCell(grid, new Pos(7, 2), 0, Color.green);

		// YELLOW
		GridCell yellow42 = new GridCell(grid, new Pos(4, 2), 0, Color.yellow);
		GridCell yellow94 = new GridCell(grid, new Pos(9, 4), 0, Color.yellow);
		
		// NEED A 10x10 GRID
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red28);
		initialFlowPointers.add(red53);
		initialFlowPointers.add(blue78);
		initialFlowPointers.add(blue81);
		initialFlowPointers.add(cyan33);
		initialFlowPointers.add(cyan77);
		initialFlowPointers.add(orange34);
		initialFlowPointers.add(orange68);
		initialFlowPointers.add(green36);
		initialFlowPointers.add(green72);
		initialFlowPointers.add(yellow42);
		initialFlowPointers.add(yellow94);	
		return initialFlowPointers;
	}
}
