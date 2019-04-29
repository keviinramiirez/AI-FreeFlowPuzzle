package util;

import java.awt.Color;
import java.util.ArrayList;

import app.GridCell;
import app.Pos;

public class HardCodeFlowPointers 
{
//	GridCell(Pos pos, Color color, int heuristic,
//			 GridCell pairFlowPointer, GridCell previousPointer)
	
	public ArrayList<GridCell> initialFlowPointers1 = new ArrayList<GridCell>();
	
	// RED
	GridCell red00 = new GridCell(new Pos(0, 0), 0, Color.red);
	GridCell red28 = new GridCell(new Pos(2, 8), 0, Color.red);
	GridCell red53 = new GridCell(new Pos(5, 3), 0, Color.red);

	// BLUE
	GridCell blue78 = new GridCell(new Pos(7, 8), 0, Color.blue);
	GridCell blue81 = new GridCell(new Pos(8, 1), 0, Color.blue);

	
	// CYAN
	GridCell cyan33 = new GridCell(new Pos(3, 3), 0, Color.cyan);
	GridCell cyan77 = new GridCell(new Pos(7, 7), 0, Color.cyan);

	
	// ORANGE
	GridCell orange34 = new GridCell(new Pos(3, 4), 0, Color.orange);
	GridCell orange68 = new GridCell(new Pos(6, 8), 0, Color.orange);

	
	// GREEN
	GridCell green36 = new GridCell(new Pos(3, 6), 0, Color.green);
	GridCell green72 = new GridCell(new Pos(7, 2), 0, Color.green);

	// YELLOW
	GridCell yellow42 = new GridCell(new Pos(4, 2), 0, Color.yellow);
	GridCell yellow94 = new GridCell(new Pos(9, 4), 0, Color.yellow);


	
	public HardCodeFlowPointers() {
		
	}
	
	public ArrayList<GridCell> generateInitialFlowPointers1() {
		// NEED A 10x10 GRID
		initialFlowPointers1.add(red28);
		initialFlowPointers1.add(red53);
		initialFlowPointers1.add(blue78);
		initialFlowPointers1.add(blue81);
		initialFlowPointers1.add(cyan33);
		initialFlowPointers1.add(cyan77);
		initialFlowPointers1.add(orange34);
		initialFlowPointers1.add(orange68);
		initialFlowPointers1.add(green36);
		initialFlowPointers1.add(green72);
		initialFlowPointers1.add(yellow42);
		initialFlowPointers1.add(yellow94);	
		return initialFlowPointers1;
	}
}
