package util;

import java.awt.Color;
import java.util.ArrayList;

import app.Grid;
import app.GridCell;
import app.Pos;

public class HardCodedFlowPointers 
{
	private Grid grid;
		
	public HardCodedFlowPointers(Grid grid) {
		this.grid = grid;
	}
	
	public ArrayList<GridCell> generateInitFlowPointers1() {
		// RED
		GridCell red28 = new GridCell(grid, new Pos(2, 8), Color.red, 0);
		GridCell red53 = new GridCell(grid, new Pos(5, 3), Color.red, 0);

		// BLUE
		GridCell blue78 = new GridCell(grid, new Pos(7, 8), Color.blue, 0);
		GridCell blue81 = new GridCell(grid, new Pos(8, 1), Color.blue, 0);

		
		// CYAN
		GridCell cyan33 = new GridCell(grid, new Pos(3, 3), Color.cyan, 0);
		GridCell cyan77 = new GridCell(grid, new Pos(7, 7), Color.cyan, 0);

		
		// ORANGE
		GridCell orange34 = new GridCell(grid, new Pos(3, 4), Color.orange, 0);
		GridCell orange68 = new GridCell(grid, new Pos(6, 8), Color.orange, 0);

		
		// GREEN
		GridCell green36 = new GridCell(grid, new Pos(3, 6), Color.green, 0);
		GridCell green72 = new GridCell(grid, new Pos(7, 2), Color.green, 0);

		// YELLOW
		GridCell yellow42 = new GridCell(grid, new Pos(4, 2), Color.yellow, 0);
		GridCell yellow94 = new GridCell(grid, new Pos(9, 4), Color.yellow, 0);
		
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
	
	public ArrayList<GridCell> generateStrandedRegion1() {
		ArrayList<GridCell> generatedCells = generateInitFlowPointers1();
		
		GridCell orange34 = generatedCells.get(6);
		GridCell orange24 = cellToModify(new Pos(2, 4), Color.orange, orange34, orange34.pairInitialFlowPointer, generatedCells);
		GridCell orange14 = cellToModify(new Pos(1, 4), Color.orange, orange24, orange24.pairInitialFlowPointer, generatedCells);

		GridCell yellow42 = generatedCells.get(10);
		GridCell yellow32 = cellToModify(new Pos(3, 2), Color.yellow, yellow42, yellow42.pairInitialFlowPointer, generatedCells);
		GridCell yellow22 = cellToModify(new Pos(2, 2), Color.yellow, yellow32, yellow32.pairInitialFlowPointer, generatedCells);
		GridCell yellow12 = cellToModify(new Pos(1, 2), Color.yellow, yellow22, yellow22.pairInitialFlowPointer, generatedCells);
		GridCell yellow02 = cellToModify(new Pos(0, 2), Color.yellow, yellow12, yellow12.pairInitialFlowPointer, generatedCells);
		GridCell yellow03 = cellToModify(new Pos(0, 3), Color.yellow, yellow02, yellow02.pairInitialFlowPointer, generatedCells);
		GridCell yellow04 = cellToModify(new Pos(0, 4), Color.yellow, yellow03, yellow03.pairInitialFlowPointer, generatedCells);

		return generatedCells;
	}
	
	public GridCell cellToModify(Pos pos, Color color, GridCell previous, GridCell pairPointer, ArrayList<GridCell> toAddTo) {
		GridCell cell = grid.gridCells[pos.row][pos.col];
		cell.color = color;
		cell.previousPointer = previous;
		cell.pairInitialFlowPointer = pairPointer;
		toAddTo.add(cell);
		return cell;
	}
}
