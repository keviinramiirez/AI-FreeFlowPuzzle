package app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import util.PuzzleCreator;

public class tester 
{
	static public void main(String[] args) {
		PuzzleCreator puzzleCreator = new PuzzleCreator();
		
		String nDimensions = "5x5";
		Iterator<List<String>> puzzleIterator = puzzleCreator.puzzleIterator(nDimensions);
		
		int rows = Integer.parseInt(nDimensions.substring(0, 1));
		int cols = Integer.parseInt(nDimensions.substring(2));

		int counter = 0;
		while (puzzleIterator.hasNext()) {
			Grid grid = new Grid();

			App.gridPanel = new JPanel[rows][cols];

			// create grid with empty cells
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < cols; c++) {
					grid.getGridCells()[r][c] = new GridCell(grid, new Pos(r, c));
					App.gridPanel[r][c] = new JPanel();
					App.gridPanel[r][c].setBorder(new LineBorder(new Color(0, 0, 0)));
				}
			
			List<GridCell> initialFlowPointer = puzzleCreator.stringToGridCells(grid, puzzleIterator.next());
			
			// initialize flow pointer
			initGridFlowPointers(grid, initialFlowPointer);

			// inserts the most constraint Initial Pointers within the priority queue
			queueMostConstraintInitialPointers(grid);
			
			Solver solver = new Solver(grid);

			float starting_time = System.currentTimeMillis()/1000;
			solver.solve();
			float ending_time = System.currentTimeMillis()/1000;
			float elapse_time = ending_time - starting_time;
			System.out.println("puzzle "+ (++counter) +" total runtime time: "+ elapse_time);
		}
	}
	
	/**
	 * initializes the grid with given flow pointer and repaints the grid panel.
	 * Also, connects each pointer with its pair pairPointer property of the flow
	 * pointers that have same colors.
	 */
	private static void initGridFlowPointers(Grid grid, List<GridCell> genInitialFlowPointers) {
		HashMap<Color, GridCell> cellPainted = new HashMap<>();
		grid.nEmptyCells -= genInitialFlowPointers.size();

		// paints UI grid and sets each pairFlowPointer property of the Initial Pointers
		for (GridCell currInitPointer : genInitialFlowPointers) {
			int row = currInitPointer.pos.row;
			int col = currInitPointer.pos.col;

			// Grid Class
			grid.getInitialFlowPointers().add(currInitPointer);
			grid.getGridCells()[row][col] = currInitPointer;

			// process to set pairFlowPointer property of each pair of FlowPointers
			if (cellPainted.containsKey(currInitPointer.color)) {
				GridCell pairPointer = cellPainted.get(currInitPointer.color);
				currInitPointer.pairFlowPointer = pairPointer;
				pairPointer.pairFlowPointer = currInitPointer;
				cellPainted.remove(currInitPointer.color);
			} else
				cellPainted.put(currInitPointer.color, currInitPointer);
		}
	}


	/**
	 * Queues the most constraint initial pointer of each pair. <br>
	 * *Note: The more non-empty adjacent cells, the more constraint it is.
	 */
	private static void queueMostConstraintInitialPointers(Grid grid) {
		LinkedList<GridCell> visitedCells = new LinkedList<>();

		// count amount of adjacent cells of each initial pointer and
		// inserts the most constraint of each pair to the priority queue.
		for (GridCell initGridCell : grid.getInitialFlowPointers()) {
			// Initial Flow Pointers
			GridCell ifp = grid.getGridCells()[initGridCell.pos.row][initGridCell.pos.col];
			int emptydjCount = ifp.getEmptyAdjs().size();

			// 
			if (visitedCells.contains(ifp.pairFlowPointer)) {
				int pairPointer_emptyAdjCount = ifp.pairFlowPointer.getEmptyAdjs().size();
				ifp.heuristic = emptydjCount;
				ifp.pairFlowPointer.heuristic = pairPointer_emptyAdjCount;
				// inserts the most constraint initial pointer of this pair
				if (emptydjCount < pairPointer_emptyAdjCount)
					grid.pq.add(ifp);
				else
					grid.pq.add(ifp.pairFlowPointer);
			} else
				visitedCells.add(ifp);
		}
	}
}
