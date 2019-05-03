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
	
	public ArrayList<GridCell> initialPointers1_10x10() {
		// RED
		GridCell red28 = new GridCell(grid, new Pos(2, 8), Color.red);
		GridCell red53 = new GridCell(grid, new Pos(5, 3), Color.red);

		// BLUE
		GridCell blue81 = new GridCell(grid, new Pos(8, 1), Color.blue);
		GridCell blue88 = new GridCell(grid, new Pos(8, 8), Color.blue);

		
		// CYAN
		GridCell cyan33 = new GridCell(grid, new Pos(3, 3), Color.cyan);
		GridCell cyan77 = new GridCell(grid, new Pos(7, 7), Color.cyan);

		
		// ORANGE
		GridCell orange34 = new GridCell(grid, new Pos(3, 4), Color.orange);
		GridCell orange68 = new GridCell(grid, new Pos(6, 8), Color.orange);

		
		// GREEN
		GridCell green36 = new GridCell(grid, new Pos(3, 6), Color.green);
		GridCell green72 = new GridCell(grid, new Pos(7, 2), Color.green);

		// YELLOW
		GridCell yellow42 = new GridCell(grid, new Pos(4, 2), Color.yellow);
		GridCell yellow94 = new GridCell(grid, new Pos(9, 4), Color.yellow);
		
		// NEED A 10x10 GRID
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red28);
		initialFlowPointers.add(red53);
		initialFlowPointers.add(blue88);
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
		ArrayList<GridCell> generatedCells = initialPointers1_10x10();
		
		GridCell orange34 = generatedCells.get(6);
		GridCell orange24 = cellToModify(new Pos(2, 4), Color.orange, orange34, orange34.pairFlowPointer, generatedCells);
		GridCell orange14 = cellToModify(new Pos(1, 4), Color.orange, orange24, orange24.pairFlowPointer, generatedCells);

		GridCell yellow42 = generatedCells.get(10);
		GridCell yellow32 = cellToModify(new Pos(3, 2), Color.yellow, yellow42, yellow42.pairFlowPointer, generatedCells);
		GridCell yellow22 = cellToModify(new Pos(2, 2), Color.yellow, yellow32, yellow32.pairFlowPointer, generatedCells);
		GridCell yellow12 = cellToModify(new Pos(1, 2), Color.yellow, yellow22, yellow22.pairFlowPointer, generatedCells);
		GridCell yellow02 = cellToModify(new Pos(0, 2), Color.yellow, yellow12, yellow12.pairFlowPointer, generatedCells);
		GridCell yellow03 = cellToModify(new Pos(0, 3), Color.yellow, yellow02, yellow02.pairFlowPointer, generatedCells);
		GridCell yellow04 = cellToModify(new Pos(0, 4), Color.yellow, yellow03, yellow03.pairFlowPointer, generatedCells);

		return generatedCells;
	}
	
	
	public ArrayList<GridCell> initialPointers2_7x7() {
		Grid.ROWS = 7;
		Grid.COLS = 7;

		// RED
		GridCell red33 = new GridCell(grid, new Pos(3, 3), Color.red);
		GridCell red64 = new GridCell(grid, new Pos(6, 4), Color.red);

		// BLUE
		GridCell blue24 = new GridCell(grid, new Pos(2, 4), Color.blue);
		GridCell blue52 = new GridCell(grid, new Pos(5, 2), Color.blue);
		
		// GREEN
		GridCell green01 = new GridCell(grid, new Pos(0, 1), Color.green);
		GridCell green51 = new GridCell(grid, new Pos(5, 1), Color.green);

		// YELLOW
		GridCell yellow22 = new GridCell(grid, new Pos(2, 2), Color.yellow);
		GridCell yellow50 = new GridCell(grid, new Pos(5, 0), Color.yellow);
		
		// NEED A 10x10 GRID
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red33);
		initialFlowPointers.add(red64);
		initialFlowPointers.add(blue24);
		initialFlowPointers.add(blue52);
		initialFlowPointers.add(green01);
		initialFlowPointers.add(green51);
		initialFlowPointers.add(yellow22);
		initialFlowPointers.add(yellow50);
		
		return initialFlowPointers;
	}
	
	public GridCell cellToModify(Pos pos, Color color, GridCell previous, GridCell pairPointer, ArrayList<GridCell> toAddTo) {
		GridCell cell = grid.gridCells[pos.row][pos.col];
		cell.color = color;
		cell.previousCell = previous;
		cell.pairFlowPointer = pairPointer;
		toAddTo.add(cell);
		return cell;
	}
}
