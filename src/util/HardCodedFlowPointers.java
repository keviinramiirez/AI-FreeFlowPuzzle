package util;

import java.awt.Color;
import java.util.ArrayList;

import app.Grid;
import app.GridCell;
import app.Pos;

public class HardCodedFlowPointers {
	private Grid grid;

	public HardCodedFlowPointers(Grid grid) {
		this.grid = grid;
	}

	public ArrayList<GridCell> initialPointers_5x5() {
		// RED
		GridCell red00 = new GridCell(grid, new Pos(0, 0), Color.red);
		GridCell red41 = new GridCell(grid, new Pos(4, 1), Color.red);

		// BLUE
		GridCell blue11 = new GridCell(grid, new Pos(1, 2), Color.blue);
		GridCell blue42 = new GridCell(grid, new Pos(4, 2), Color.blue);

		// GREEN
		GridCell green02 = new GridCell(grid, new Pos(0, 2), Color.green);
		GridCell green31 = new GridCell(grid, new Pos(3, 1), Color.green);

		// YELLOW
		GridCell yellow04 = new GridCell(grid, new Pos(0, 4), Color.yellow);
		GridCell yellow33 = new GridCell(grid, new Pos(3, 3), Color.yellow);

		// ORANGE
		GridCell orange14 = new GridCell(grid, new Pos(1, 4), Color.orange);
		GridCell orange43 = new GridCell(grid, new Pos(4, 3), Color.orange);

		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red00);
		initialFlowPointers.add(red41);
		initialFlowPointers.add(blue11);
		initialFlowPointers.add(blue42);
		initialFlowPointers.add(green02);
		initialFlowPointers.add(green31);
		initialFlowPointers.add(yellow04);
		initialFlowPointers.add(yellow33);
		initialFlowPointers.add(orange14);
		initialFlowPointers.add(orange43);
		return initialFlowPointers;
	}

	public ArrayList<GridCell> initialPointers_6x6() {
		// RED
		GridCell red20 = new GridCell(grid, new Pos(2, 0), Color.red);
		GridCell red11 = new GridCell(grid, new Pos(1, 1), Color.red);

		// BLUE
		GridCell blue10 = new GridCell(grid, new Pos(1, 0), Color.blue);
		GridCell blue35 = new GridCell(grid, new Pos(3, 5), Color.blue);

		// GREEN
		GridCell green30 = new GridCell(grid, new Pos(3, 0), Color.green);
		GridCell green23 = new GridCell(grid, new Pos(2, 3), Color.green);

		// YELLOW
		GridCell yellow12 = new GridCell(grid, new Pos(1, 2), Color.yellow);
		GridCell yellow41 = new GridCell(grid, new Pos(4, 1), Color.yellow);

		// ORANGE
		GridCell orange13 = new GridCell(grid, new Pos(1, 3), Color.orange);
		GridCell orange44 = new GridCell(grid, new Pos(4, 4), Color.orange);

		// CYAN
		GridCell cyan53 = new GridCell(grid, new Pos(5, 3), Color.cyan);
		GridCell cyan45 = new GridCell(grid, new Pos(4, 5), Color.cyan);
		
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red20);
		initialFlowPointers.add(red11);
		initialFlowPointers.add(blue10);
		initialFlowPointers.add(blue35);
		initialFlowPointers.add(green30);
		initialFlowPointers.add(green23);
		initialFlowPointers.add(yellow12);
		initialFlowPointers.add(yellow41);
		initialFlowPointers.add(orange13);
		initialFlowPointers.add(orange44);
		initialFlowPointers.add(cyan53);
		initialFlowPointers.add(cyan45);
		return initialFlowPointers;
	}

	public ArrayList<GridCell> initialPointers_7x7() {
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
	
	public ArrayList<GridCell> initialPointers_7x7_V2() {
		// RED
		GridCell red33 = new GridCell(grid, new Pos(2, 6), Color.red);
		GridCell red64 = new GridCell(grid, new Pos(5, 1), Color.red);

		// BLUE
		GridCell blue24 = new GridCell(grid, new Pos(2, 5), Color.blue);
		GridCell blue52 = new GridCell(grid, new Pos(5, 3), Color.blue);

		// GREEN
		GridCell green01 = new GridCell(grid, new Pos(1, 1), Color.green);
		GridCell green51 = new GridCell(grid, new Pos(5, 2), Color.green);

		// YELLOW
		GridCell yellow22 = new GridCell(grid, new Pos(1, 5), Color.yellow);
		GridCell yellow50 = new GridCell(grid, new Pos(2, 2), Color.yellow);
		
		// MAGENTA
		GridCell magenta56 = new GridCell(grid, new Pos(5, 0), Color.magenta);
		GridCell magenta36 = new GridCell(grid, new Pos(3, 6), Color.magenta);
		
		// CYAN
		GridCell cyan66 = new GridCell(grid, new Pos(6, 6), Color.cyan);
		GridCell cyan46 = new GridCell(grid, new Pos(4, 6), Color.cyan);
		
		// ORANGE
		GridCell orange45 = new GridCell(grid, new Pos(4, 5), Color.orange);
		GridCell orange65 = new GridCell(grid, new Pos(6, 5), Color.orange);

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
		initialFlowPointers.add(magenta56);
		initialFlowPointers.add(magenta36);
		initialFlowPointers.add(cyan46);
		initialFlowPointers.add(cyan66);
		initialFlowPointers.add(orange45);
		initialFlowPointers.add(orange65);

		return initialFlowPointers;
	}

	
	public ArrayList<GridCell> initialPointers_8x8_V3() {
		// RED
		GridCell red16 = new GridCell(grid, new Pos(1, 6), Color.red);
		GridCell red34 = new GridCell(grid, new Pos(3, 4), Color.red);

		// BLUE
		GridCell blue06 = new GridCell(grid, new Pos(0, 6), Color.blue);
		GridCell blue22 = new GridCell(grid, new Pos(2, 2), Color.blue);

		// GREEN
		GridCell green23 = new GridCell(grid, new Pos(2, 3), Color.green);
		GridCell green66 = new GridCell(grid, new Pos(6, 6), Color.green);

		// YELLOW
		GridCell yellow45 = new GridCell(grid, new Pos(4, 5), Color.yellow);
		GridCell yellow71 = new GridCell(grid, new Pos(7, 1), Color.yellow);

		// ORANGE
		GridCell orange04 = new GridCell(grid, new Pos(0, 4), Color.orange);
		GridCell orange15 = new GridCell(grid, new Pos(1, 5), Color.orange);

		// CYAN
		GridCell cyan12 = new GridCell(grid, new Pos(1, 2), Color.cyan);
		GridCell cyan61 = new GridCell(grid, new Pos(6, 1), Color.cyan);
		// NEED A 10x10 GRID
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red16);
		initialFlowPointers.add(red34);
		initialFlowPointers.add(blue06);
		initialFlowPointers.add(blue22);
		initialFlowPointers.add(green23);
		initialFlowPointers.add(green66);
		initialFlowPointers.add(yellow45);
		initialFlowPointers.add(yellow71);
		initialFlowPointers.add(orange04);
		initialFlowPointers.add(orange15);
		initialFlowPointers.add(cyan12);
		initialFlowPointers.add(cyan61);

		return initialFlowPointers;
	}
	
	public ArrayList<GridCell> initialPointers_8x8() {
		// RED
		GridCell red46 = new GridCell(grid, new Pos(4, 6), Color.red);
		GridCell red50 = new GridCell(grid, new Pos(5, 0), Color.red);

		// BLUE
		GridCell blue60 = new GridCell(grid, new Pos(6, 0), Color.blue);
		GridCell blue33 = new GridCell(grid, new Pos(3, 3), Color.blue);

		// GREEN
		GridCell green36 = new GridCell(grid, new Pos(3, 6), Color.green);
		GridCell green10 = new GridCell(grid, new Pos(1, 0), Color.green);

		// YELLOW
		GridCell yellow11 = new GridCell(grid, new Pos(1, 1), Color.green);
		GridCell yellow64 = new GridCell(grid, new Pos(6, 4), Color.green);
		
		// CYAN
		GridCell cyan76 = new GridCell(grid, new Pos(7, 6), Color.cyan);
		GridCell cyan07 = new GridCell(grid, new Pos(0, 7), Color.cyan);

		// ORANGE
		GridCell orange31 = new GridCell(grid, new Pos(3, 1), Color.orange);
		GridCell orange61 = new GridCell(grid, new Pos(6, 1), Color.orange);

		// NEED A 10x10 GRID
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red46);
		initialFlowPointers.add(red50);
		initialFlowPointers.add(blue60);
		initialFlowPointers.add(blue33);
		initialFlowPointers.add(green36);
		initialFlowPointers.add(green10);
		initialFlowPointers.add(yellow11);
		initialFlowPointers.add(yellow64);
		initialFlowPointers.add(cyan76);
		initialFlowPointers.add(cyan07);
		initialFlowPointers.add(orange31);
		initialFlowPointers.add(orange61);

		return initialFlowPointers;
	}

	public ArrayList<GridCell> initialPointers_8x8_V2() {
		// RED
		GridCell red16 = new GridCell(grid, new Pos(1, 6), Color.red);
		GridCell red34 = new GridCell(grid, new Pos(3, 4), Color.red);

		// BLUE
		GridCell blue06 = new GridCell(grid, new Pos(0, 6), Color.blue);
		GridCell blue22 = new GridCell(grid, new Pos(2, 2), Color.blue);

		// GREEN
		GridCell green23 = new GridCell(grid, new Pos(2, 3), Color.green);
		GridCell green66 = new GridCell(grid, new Pos(6, 6), Color.green);

		// YELLOW
		GridCell yellow45 = new GridCell(grid, new Pos(4, 5), Color.yellow);
		GridCell yellow71 = new GridCell(grid, new Pos(7, 1), Color.yellow);

		// ORANGE
		GridCell orange04 = new GridCell(grid, new Pos(0, 4), Color.orange);
		GridCell orange15 = new GridCell(grid, new Pos(1, 5), Color.orange);

		// CYAN
		GridCell cyan12 = new GridCell(grid, new Pos(1, 2), Color.cyan);
		GridCell cyan61 = new GridCell(grid, new Pos(6, 1), Color.cyan);
		// NEED A 10x10 GRID
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		initialFlowPointers.add(red16);
		initialFlowPointers.add(red34);
		initialFlowPointers.add(blue06);
		initialFlowPointers.add(blue22);
		initialFlowPointers.add(green23);
		initialFlowPointers.add(green66);
		initialFlowPointers.add(yellow45);
		initialFlowPointers.add(yellow71);
		initialFlowPointers.add(orange04);
		initialFlowPointers.add(orange15);
		initialFlowPointers.add(cyan12);
		initialFlowPointers.add(cyan61);

		return initialFlowPointers;
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
		GridCell orange24 = cellToModify(new Pos(2, 4), Color.orange, orange34, orange34.pairFlowPointer,
				generatedCells);
		GridCell orange14 = cellToModify(new Pos(1, 4), Color.orange, orange24, orange24.pairFlowPointer,
				generatedCells);

		GridCell yellow42 = generatedCells.get(10);
		GridCell yellow32 = cellToModify(new Pos(3, 2), Color.yellow, yellow42, yellow42.pairFlowPointer,
				generatedCells);
		GridCell yellow22 = cellToModify(new Pos(2, 2), Color.yellow, yellow32, yellow32.pairFlowPointer,
				generatedCells);
		GridCell yellow12 = cellToModify(new Pos(1, 2), Color.yellow, yellow22, yellow22.pairFlowPointer,
				generatedCells);
		GridCell yellow02 = cellToModify(new Pos(0, 2), Color.yellow, yellow12, yellow12.pairFlowPointer,
				generatedCells);
		GridCell yellow03 = cellToModify(new Pos(0, 3), Color.yellow, yellow02, yellow02.pairFlowPointer,
				generatedCells);
		GridCell yellow04 = cellToModify(new Pos(0, 4), Color.yellow, yellow03, yellow03.pairFlowPointer,
				generatedCells);

		return generatedCells;
	}

	public GridCell cellToModify(Pos pos, Color color, GridCell previous, GridCell pairPointer,
			ArrayList<GridCell> toAddTo) {
		GridCell cell = grid.gridCells[pos.row][pos.col];
		cell.color = color;
		cell.previousCell = previous;
		cell.pairFlowPointer = pairPointer;
		toAddTo.add(cell);
		return cell;
	}
}
